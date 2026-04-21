const state = {
    token: localStorage.getItem("adsToken"),
    user: JSON.parse(localStorage.getItem("adsUser") || "null"),
    patients: [],
    dentists: [],
    surgeries: [],
    appointments: [],
    bills: []
};

const byId = (id) => document.getElementById(id);

function authHeaders() {
    return {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${state.token}`
    };
}

async function api(path, options = {}) {
    const response = await fetch(path, {
        ...options,
        headers: {
            ...(options.headers || {}),
            ...(state.token ? authHeaders() : {"Content-Type": "application/json"})
        }
    });

    if (response.status === 401) {
        logout();
        throw new Error("Please sign in again.");
    }

    const text = await response.text();
    const body = text ? JSON.parse(text) : null;
    if (!response.ok) {
        throw new Error(body?.message || "Request failed.");
    }
    return body;
}

function showMessage(text, type = "success") {
    const message = byId("message");
    message.textContent = text;
    message.className = `message ${type}`;
    window.setTimeout(() => message.classList.add("hidden"), 5200);
}

function setSignedInUi() {
    const signedIn = Boolean(state.token);
    byId("loginForm").classList.toggle("hidden", signedIn);
    byId("userPanel").classList.toggle("hidden", !signedIn);
    if (state.user) {
        byId("userName").textContent = state.user.fullName;
        byId("userRole").textContent = state.user.role;
    }
}

async function login(event) {
    event.preventDefault();
    try {
        const response = await api("/api/v1/auth/login", {
            method: "POST",
            body: JSON.stringify({
                username: byId("username").value,
                password: byId("password").value
            })
        });
        state.token = response.accessToken;
        state.user = response;
        localStorage.setItem("adsToken", state.token);
        localStorage.setItem("adsUser", JSON.stringify(response));
        setSignedInUi();
        await loadAll();
        showMessage("Signed in and loaded current clinic data.", "success");
    } catch (error) {
        showMessage(error.message, "error");
    }
}

function logout() {
    state.token = null;
    state.user = null;
    localStorage.removeItem("adsToken");
    localStorage.removeItem("adsUser");
    setSignedInUi();
}

async function loadAll() {
    if (!state.token) {
        setSignedInUi();
        return;
    }
    const [dashboard, patients, dentists, surgeries, appointments, bills] = await Promise.all([
        api("/api/v1/dashboard"),
        api("/api/v1/patients"),
        api("/api/v1/dentists"),
        api("/api/v1/surgeries"),
        api("/api/v1/appointments"),
        api("/api/v1/bills")
    ]);

    state.patients = patients;
    state.dentists = dentists;
    state.surgeries = surgeries;
    state.appointments = appointments;
    state.bills = bills;
    renderDashboard(dashboard);
    renderPatients(patients);
    renderAppointments(appointments);
    renderBills();
    fillSelects();
}

function renderDashboard(dashboard) {
    const metrics = [
        ["Patients", dashboard.patientCount],
        ["Dentists", dashboard.dentistCount],
        ["Surgeries", dashboard.surgeryCount],
        ["Appointments", dashboard.appointmentCount],
        ["Unpaid Bills", dashboard.unpaidBillCount]
    ];

    byId("metricGrid").innerHTML = metrics
        .map(([label, value]) => `<article class="metric-card"><span>${label}</span><strong>${value}</strong></article>`)
        .join("");

    byId("upcomingRows").innerHTML = rowsForAppointments(dashboard.upcomingAppointments, false);
}

function rowsForAppointments(appointments, withAction) {
    if (!appointments.length) {
        return `<tr><td colspan="${withAction ? 7 : 6}">No appointments found.</td></tr>`;
    }
    return appointments.map((appointment) => {
        const statusClass = appointment.status.toLowerCase();
        const action = withAction
            ? `<td><button class="link-button" data-cancel="${appointment.appointmentId}" type="button">Cancel</button></td>`
            : "";
        return `
            <tr>
                <td>${appointment.appointmentDate}</td>
                <td>${appointment.appointmentTime}</td>
                <td>${appointment.patient.fullName}</td>
                <td>${appointment.dentist.fullName}</td>
                <td>${appointment.surgery.surgeryNumber} - ${appointment.surgery.name}</td>
                <td><span class="status ${statusClass}">${appointment.status}</span></td>
                ${action}
            </tr>
        `;
    }).join("");
}

function renderAppointments(appointments) {
    byId("appointmentRows").innerHTML = rowsForAppointments(appointments, true);
}

function formatMoney(amount) {
    return Number(amount).toLocaleString("en-US", {
        style: "currency",
        currency: "USD"
    });
}

function renderBills() {
    const selectedStatus = byId("billStatusFilter").value;
    const bills = selectedStatus
        ? state.bills.filter((bill) => bill.status === selectedStatus)
        : state.bills;

    if (!bills.length) {
        byId("billRows").innerHTML = "<tr><td colspan=\"7\">No bills found.</td></tr>";
        return;
    }

    byId("billRows").innerHTML = bills.map((bill) => {
        const statusClass = bill.status.toLowerCase();
        const primaryAction = bill.status === "UNPAID"
            ? `<button class="link-button" data-bill="${bill.billId}" data-status="PAID" type="button">Mark Paid</button>`
            : bill.status === "PAID"
                ? `<button class="link-button" data-bill="${bill.billId}" data-status="UNPAID" type="button">Mark Unpaid</button>`
                : "";
        const voidAction = bill.status !== "VOID"
            ? `<button class="link-button quiet-button" data-bill="${bill.billId}" data-status="VOID" type="button">Void</button>`
            : "";
        return `
            <tr>
                <td>${bill.patient.patientNumber} - ${bill.patient.fullName}</td>
                <td>${bill.appointmentLabel}</td>
                <td>${bill.issueDate}</td>
                <td>${formatMoney(bill.amount)}</td>
                <td><span class="status ${statusClass}">${bill.status}</span></td>
                <td>${bill.description}</td>
                <td class="action-cell">${primaryAction}${voidAction}</td>
            </tr>
        `;
    }).join("");
}

function renderPatients(patients) {
    if (!patients.length) {
        byId("patientRows").innerHTML = "<tr><td colspan=\"5\">No patients found.</td></tr>";
        return;
    }
    byId("patientRows").innerHTML = patients.map((patient) => `
        <tr>
            <td>${patient.patientNumber}</td>
            <td>${patient.fullName}</td>
            <td>${patient.phoneNumber}</td>
            <td>${patient.email}</td>
            <td>${patient.mailingAddress.street}, ${patient.mailingAddress.city}, ${patient.mailingAddress.state} ${patient.mailingAddress.zipCode}</td>
        </tr>
    `).join("");
}

function fillSelects() {
    byId("appointmentPatient").innerHTML = state.patients
        .map((patient) => `<option value="${patient.patientId}">${patient.patientNumber} - ${patient.fullName}</option>`)
        .join("");
    byId("appointmentDentist").innerHTML = state.dentists
        .map((dentist) => `<option value="${dentist.dentistId}">${dentist.dentistCode} - ${dentist.fullName}</option>`)
        .join("");
    byId("appointmentSurgery").innerHTML = state.surgeries
        .map((surgery) => `<option value="${surgery.surgeryId}">${surgery.surgeryNumber} - ${surgery.name}</option>`)
        .join("");
    byId("billPatient").innerHTML = state.patients
        .map((patient) => `<option value="${patient.patientId}">${patient.patientNumber} - ${patient.fullName}</option>`)
        .join("");
    fillBillAppointmentSelect();
}

function fillBillAppointmentSelect() {
    const patientId = Number(byId("billPatient").value);
    const appointmentOptions = state.appointments
        .filter((appointment) => appointment.patient.patientId === patientId)
        .map((appointment) => `<option value="${appointment.appointmentId}">
            ${appointment.appointmentDate} ${appointment.appointmentTime} - ${appointment.dentist.fullName}
        </option>`)
        .join("");
    byId("billAppointment").innerHTML = `<option value="">Manual bill / no appointment</option>${appointmentOptions}`;
}

async function scheduleAppointment(event) {
    event.preventDefault();
    try {
        await api("/api/v1/appointments", {
            method: "POST",
            body: JSON.stringify({
                patientId: Number(byId("appointmentPatient").value),
                dentistId: Number(byId("appointmentDentist").value),
                surgeryId: Number(byId("appointmentSurgery").value),
                appointmentDate: byId("appointmentDate").value,
                appointmentTime: byId("appointmentTime").value,
                reason: byId("appointmentReason").value
            })
        });
        byId("appointmentForm").reset();
        setDefaultAppointmentDate();
        await loadAll();
        showMessage("Appointment scheduled and confirmation marked as sent.", "success");
    } catch (error) {
        showMessage(error.message, "error");
    }
}

async function createPatient(event) {
    event.preventDefault();
    try {
        await api("/api/v1/patients", {
            method: "POST",
            body: JSON.stringify({
                patientNumber: byId("patientNumber").value,
                firstName: byId("firstName").value,
                lastName: byId("lastName").value,
                phoneNumber: byId("phoneNumber").value,
                email: byId("email").value,
                dateOfBirth: byId("dateOfBirth").value,
                mailingAddress: {
                    street: byId("street").value,
                    city: byId("city").value,
                    state: byId("state").value,
                    zipCode: byId("zipCode").value
                }
            })
        });
        byId("patientForm").reset();
        byId("city").value = "Fairfield";
        byId("state").value = "IA";
        byId("zipCode").value = "52556";
        await loadAll();
        showMessage("Patient registered successfully.", "success");
        selectTab("patients");
    } catch (error) {
        showMessage(error.message, "error");
    }
}

async function createBill(event) {
    event.preventDefault();
    try {
        const appointmentId = byId("billAppointment").value;
        await api("/api/v1/bills", {
            method: "POST",
            body: JSON.stringify({
                patientId: Number(byId("billPatient").value),
                appointmentId: appointmentId ? Number(appointmentId) : null,
                issueDate: byId("billIssueDate").value,
                amount: Number(byId("billAmount").value),
                status: byId("billStatus").value,
                description: byId("billDescription").value
            })
        });
        byId("billForm").reset();
        byId("billAmount").value = "120.00";
        byId("billStatus").value = "UNPAID";
        byId("billDescription").value = "Dental service balance";
        setDefaultBillIssueDate();
        await loadAll();
        showMessage("Dental bill created. Unpaid bills will block new appointment booking.", "success");
        selectTab("billing");
    } catch (error) {
        showMessage(error.message, "error");
    }
}

async function searchPatients(event) {
    event.preventDefault();
    try {
        const query = byId("patientSearch").value.trim();
        const patients = await api(query ? `/api/v1/patients?search=${encodeURIComponent(query)}` : "/api/v1/patients");
        renderPatients(patients);
    } catch (error) {
        showMessage(error.message, "error");
    }
}

async function cancelAppointment(appointmentId) {
    try {
        await api(`/api/v1/appointments/${appointmentId}`, {method: "DELETE"});
        await loadAll();
        showMessage("Appointment cancelled.", "success");
    } catch (error) {
        showMessage(error.message, "error");
    }
}

async function updateBillStatus(billId, status) {
    try {
        await api(`/api/v1/bills/${billId}/status`, {
            method: "PATCH",
            body: JSON.stringify({status})
        });
        await loadAll();
        showMessage(`Bill status updated to ${status}.`, "success");
        selectTab("billing");
    } catch (error) {
        showMessage(error.message, "error");
    }
}

function selectTab(name) {
    document.querySelectorAll(".tab").forEach((tab) => {
        tab.classList.toggle("active", tab.dataset.tab === name);
    });
    document.querySelectorAll(".tab-panel").forEach((panel) => {
        panel.classList.toggle("active", panel.id === name);
    });
}

function setDefaultAppointmentDate() {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    byId("appointmentDate").value = tomorrow.toISOString().slice(0, 10);
}

function setDefaultBillIssueDate() {
    byId("billIssueDate").value = new Date().toISOString().slice(0, 10);
}

document.querySelectorAll(".tab").forEach((tab) => {
    tab.addEventListener("click", () => selectTab(tab.dataset.tab));
});

byId("loginForm").addEventListener("submit", login);
byId("logoutButton").addEventListener("click", logout);
byId("refreshButton").addEventListener("click", () => loadAll().catch((error) => showMessage(error.message, "error")));
byId("appointmentForm").addEventListener("submit", scheduleAppointment);
byId("billForm").addEventListener("submit", createBill);
byId("billPatient").addEventListener("change", fillBillAppointmentSelect);
byId("billStatusFilter").addEventListener("change", renderBills);
byId("patientForm").addEventListener("submit", createPatient);
byId("searchForm").addEventListener("submit", searchPatients);
byId("appointmentRows").addEventListener("click", (event) => {
    const appointmentId = event.target.dataset.cancel;
    if (appointmentId) {
        cancelAppointment(appointmentId);
    }
});
byId("billRows").addEventListener("click", (event) => {
    const billId = event.target.dataset.bill;
    const status = event.target.dataset.status;
    if (billId && status) {
        updateBillStatus(billId, status);
    }
});

setDefaultAppointmentDate();
setDefaultBillIssueDate();
setSignedInUi();
loadAll().catch(() => setSignedInUi());
