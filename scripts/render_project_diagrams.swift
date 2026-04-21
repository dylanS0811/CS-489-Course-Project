import AppKit
import Foundation

let root = URL(fileURLWithPath: FileManager.default.currentDirectoryPath)
let outDir = root.appendingPathComponent("docs/images")

try FileManager.default.createDirectory(at: outDir, withIntermediateDirectories: true)

let ink = NSColor(calibratedRed: 37/255, green: 56/255, blue: 75/255, alpha: 1)
let muted = NSColor(calibratedRed: 90/255, green: 111/255, blue: 132/255, alpha: 1)
let blue = NSColor(calibratedRed: 75/255, green: 111/255, blue: 150/255, alpha: 1)
let headerFill = NSColor(calibratedRed: 224/255, green: 235/255, blue: 248/255, alpha: 1)
let bodyFill = NSColor(calibratedRed: 249/255, green: 252/255, blue: 255/255, alpha: 1)
let paleBlue = NSColor(calibratedRed: 243/255, green: 248/255, blue: 253/255, alpha: 1)
let paleGold = NSColor(calibratedRed: 255/255, green: 250/255, blue: 230/255, alpha: 1)
let coral = NSColor(calibratedRed: 188/255, green: 92/255, blue: 79/255, alpha: 1)
let green = NSColor(calibratedRed: 58/255, green: 126/255, blue: 79/255, alpha: 1)
let gold = NSColor(calibratedRed: 178/255, green: 132/255, blue: 24/255, alpha: 1)

func savePNG(width: Int, height: Int, filename: String, draw: (NSRect) -> Void) throws {
    let image = NSImage(size: NSSize(width: width, height: height))
    image.lockFocusFlipped(true)
    NSColor.white.setFill()
    NSRect(x: 0, y: 0, width: width, height: height).fill()
    draw(NSRect(x: 0, y: 0, width: width, height: height))
    image.unlockFocus()

    guard let tiff = image.tiffRepresentation,
          let bitmap = NSBitmapImageRep(data: tiff),
          let data = bitmap.representation(using: .png, properties: [:]) else {
        throw NSError(domain: "diagram", code: 1, userInfo: [NSLocalizedDescriptionKey: "Unable to render \(filename)"])
    }
    try data.write(to: outDir.appendingPathComponent(filename))
}

func font(_ size: CGFloat, _ weight: NSFont.Weight = .regular) -> NSFont {
    NSFont.systemFont(ofSize: size, weight: weight)
}

func mono(_ size: CGFloat, _ weight: NSFont.Weight = .regular) -> NSFont {
    NSFont.monospacedSystemFont(ofSize: size, weight: weight)
}

func text(_ value: String,
          _ rect: NSRect,
          size: CGFloat = 24,
          weight: NSFont.Weight = .regular,
          color: NSColor = ink,
          align: NSTextAlignment = .left,
          monoFace: Bool = false) {
    let paragraph = NSMutableParagraphStyle()
    paragraph.alignment = align
    paragraph.lineSpacing = 4
    let attrs: [NSAttributedString.Key: Any] = [
        .font: monoFace ? mono(size, weight) : font(size, weight),
        .foregroundColor: color,
        .paragraphStyle: paragraph
    ]
    (value as NSString).draw(in: rect, withAttributes: attrs)
}

func rounded(_ rect: NSRect,
             fill: NSColor = bodyFill,
             stroke: NSColor = blue,
             width: CGFloat = 2,
             radius: CGFloat = 12,
             dash: [CGFloat]? = nil) {
    let path = NSBezierPath(roundedRect: rect, xRadius: radius, yRadius: radius)
    fill.setFill()
    path.fill()
    stroke.setStroke()
    path.lineWidth = width
    if let dash { path.setLineDash(dash, count: dash.count, phase: 0) }
    path.stroke()
}

func rect(_ rect: NSRect,
          fill: NSColor = bodyFill,
          stroke: NSColor = blue,
          width: CGFloat = 2) {
    fill.setFill()
    NSBezierPath(rect: rect).fill()
    stroke.setStroke()
    let path = NSBezierPath(rect: rect)
    path.lineWidth = width
    path.stroke()
}

func line(_ a: CGPoint, _ b: CGPoint, color: NSColor = blue, width: CGFloat = 2, dash: [CGFloat]? = nil) {
    let path = NSBezierPath()
    path.move(to: a)
    path.line(to: b)
    color.setStroke()
    path.lineWidth = width
    if let dash { path.setLineDash(dash, count: dash.count, phase: 0) }
    path.stroke()
}

func polyline(_ points: [CGPoint], color: NSColor = blue, width: CGFloat = 2) {
    guard let first = points.first else { return }
    let path = NSBezierPath()
    path.move(to: first)
    for p in points.dropFirst() { path.line(to: p) }
    color.setStroke()
    path.lineWidth = width
    path.stroke()
}

func arrow(_ a: CGPoint, _ b: CGPoint, color: NSColor = blue, width: CGFloat = 2) {
    line(a, b, color: color, width: width)
    let angle = atan2(b.y - a.y, b.x - a.x)
    let size: CGFloat = 13
    let p1 = CGPoint(x: b.x - size * cos(angle - .pi / 6), y: b.y - size * sin(angle - .pi / 6))
    let p2 = CGPoint(x: b.x - size * cos(angle + .pi / 6), y: b.y - size * sin(angle + .pi / 6))
    let path = NSBezierPath()
    path.move(to: b)
    path.line(to: p1)
    path.line(to: p2)
    path.close()
    color.setFill()
    path.fill()
}

func inheritance(_ from: CGPoint, _ to: CGPoint) {
    line(from, to)
    let angle = atan2(to.y - from.y, to.x - from.x)
    let size: CGFloat = 17
    let p1 = CGPoint(x: to.x - size * cos(angle - .pi / 7), y: to.y - size * sin(angle - .pi / 7))
    let p2 = CGPoint(x: to.x - size * cos(angle + .pi / 7), y: to.y - size * sin(angle + .pi / 7))
    let tri = NSBezierPath()
    tri.move(to: to)
    tri.line(to: p1)
    tri.line(to: p2)
    tri.close()
    NSColor.white.setFill()
    tri.fill()
    blue.setStroke()
    tri.lineWidth = 2
    tri.stroke()
}

func classBox(_ titleValue: String, fields: [String], at r: NSRect, titleSize: CGFloat = 22) {
    rounded(r, fill: bodyFill, stroke: blue, width: 2, radius: 10)
    rect(NSRect(x: r.minX, y: r.minY, width: r.width, height: 46), fill: headerFill, stroke: blue, width: 0)
    line(CGPoint(x: r.minX, y: r.minY + 46), CGPoint(x: r.maxX, y: r.minY + 46))
    text(titleValue, NSRect(x: r.minX + 8, y: r.minY + 11, width: r.width - 16, height: 28),
         size: titleSize, weight: .bold, color: ink, align: .center)
    var y = r.minY + 62
    for f in fields {
        text("- \(f)", NSRect(x: r.minX + 18, y: y, width: r.width - 34, height: 24),
             size: 15, weight: .regular, color: ink)
        y += 23
    }
}

func tableBox(_ titleValue: String, fields: [(String, String)], at r: NSRect) {
    rounded(r, fill: bodyFill, stroke: blue, width: 2, radius: 9)
    rect(NSRect(x: r.minX, y: r.minY, width: r.width, height: 44), fill: headerFill, stroke: blue, width: 0)
    line(CGPoint(x: r.minX, y: r.minY + 44), CGPoint(x: r.maxX, y: r.minY + 44))
    text(titleValue, NSRect(x: r.minX + 8, y: r.minY + 10, width: r.width - 16, height: 26),
         size: 20, weight: .bold, color: ink, align: .center, monoFace: true)
    var y = r.minY + 60
    for (key, field) in fields {
        let keyColor: NSColor = key == "PK" ? coral : (key == "FK" ? blue : muted)
        text(key, NSRect(x: r.minX + 18, y: y, width: 45, height: 19), size: 12, weight: .bold, color: keyColor, monoFace: true)
        text(field, NSRect(x: r.minX + 62, y: y, width: r.width - 78, height: 19), size: 13, color: ink, monoFace: true)
        y += 20
    }
}

func label(_ value: String, _ point: CGPoint, size: CGFloat = 15, color: NSColor = muted, align: NSTextAlignment = .center) {
    text(value, NSRect(x: point.x - 75, y: point.y - 14, width: 150, height: 24), size: size, weight: .medium, color: color, align: align)
}

try savePNG(width: 2200, height: 1320, filename: "domain-model-class-diagram.png") { _ in
    text("BrightSmile Dental Surgery Appointment Domain Model", NSRect(x: 0, y: 34, width: 2200, height: 52),
         size: 38, weight: .bold, color: ink, align: .center)
    text("Course Project static model: people, appointments, surgeries, billing, and security roles",
         NSRect(x: 0, y: 84, width: 2200, height: 34), size: 20, color: muted, align: .center)

    let person = NSRect(x: 780, y: 145, width: 640, height: 126)
    let appUser = NSRect(x: 360, y: 365, width: 330, height: 128)
    let dentist = NSRect(x: 870, y: 365, width: 330, height: 128)
    let patient = NSRect(x: 1380, y: 365, width: 360, height: 152)
    let role = NSRect(x: 88, y: 365, width: 230, height: 104)
    let appointment = NSRect(x: 760, y: 705, width: 560, height: 178)
    let surgery = NSRect(x: 1480, y: 720, width: 360, height: 128)
    let bill = NSRect(x: 1235, y: 1030, width: 360, height: 128)
    let patientAddress = NSRect(x: 1720, y: 560, width: 360, height: 128)
    let surgeryAddress = NSRect(x: 1720, y: 930, width: 360, height: 128)
    let apptStatus = NSRect(x: 410, y: 1010, width: 270, height: 120)
    let billStatus = NSRect(x: 1640, y: 1130, width: 250, height: 96)

    classBox("Person", fields: ["firstName : String", "lastName : String", "phoneNumber : String", "email : String"], at: person)
    classBox("Role", fields: ["roleId : Long", "roleName : String"], at: role, titleSize: 20)
    classBox("AppUser", fields: ["userId : Long", "username : String", "password : String"], at: appUser)
    classBox("Dentist", fields: ["dentistId : Long", "dentistCode : String", "specialization : String"], at: dentist)
    classBox("Patient", fields: ["patientId : Long", "patientNumber : String", "dateOfBirth : LocalDate"], at: patient)
    classBox("Appointment", fields: ["appointmentId : Long", "appointmentDate : LocalDate", "appointmentTime : LocalTime", "status : AppointmentStatus", "confirmationSent : boolean", "reason : String"], at: appointment)
    classBox("Surgery", fields: ["surgeryId : Long", "surgeryNumber : String", "name : String", "phoneNumber : String"], at: surgery)
    classBox("DentalBill", fields: ["billId : Long", "issueDate : LocalDate", "amount : BigDecimal", "status : BillStatus"], at: bill)
    classBox("Address", fields: ["addressId : Long", "street : String", "city : String", "state : String", "zipCode : String"], at: patientAddress)
    classBox("Address", fields: ["addressId : Long", "street : String", "city : String", "state : String", "zipCode : String"], at: surgeryAddress)
    classBox("AppointmentStatus", fields: ["SCHEDULED", "COMPLETED", "CANCELLED"], at: apptStatus, titleSize: 19)
    classBox("BillStatus", fields: ["PAID", "UNPAID", "VOID"], at: billStatus, titleSize: 19)

    inheritance(CGPoint(x: appUser.midX, y: appUser.minY), CGPoint(x: person.minX + 130, y: person.maxY))
    inheritance(CGPoint(x: dentist.midX, y: dentist.minY), CGPoint(x: person.midX, y: person.maxY))
    inheritance(CGPoint(x: patient.midX, y: patient.minY), CGPoint(x: person.maxX - 130, y: person.maxY))

    polyline([CGPoint(x: role.maxX, y: role.midY), CGPoint(x: appUser.minX, y: appUser.midY)])
    label("1", CGPoint(x: role.maxX + 22, y: role.midY - 16))
    label("0..*", CGPoint(x: appUser.minX - 28, y: appUser.midY - 16))
    label("assigned", CGPoint(x: 339, y: role.midY - 34), size: 15)

    polyline([CGPoint(x: appUser.midX, y: appUser.maxY), CGPoint(x: appUser.midX, y: appointment.midY), CGPoint(x: appointment.minX, y: appointment.midY)])
    label("1", CGPoint(x: appUser.midX, y: appUser.maxY + 24))
    label("0..*", CGPoint(x: appointment.minX - 28, y: appointment.midY - 18))
    label("books", CGPoint(x: 640, y: appointment.midY - 36))

    polyline([CGPoint(x: dentist.midX, y: dentist.maxY), CGPoint(x: dentist.midX, y: appointment.minY)])
    label("1", CGPoint(x: dentist.midX - 30, y: dentist.maxY + 26))
    label("0..*", CGPoint(x: dentist.midX - 34, y: appointment.minY - 16))
    label("treats", CGPoint(x: dentist.midX + 54, y: 620))

    polyline([CGPoint(x: patient.midX, y: patient.maxY), CGPoint(x: patient.midX, y: appointment.midY), CGPoint(x: appointment.maxX, y: appointment.midY)])
    label("1", CGPoint(x: patient.midX + 30, y: patient.maxY + 26))
    label("0..*", CGPoint(x: appointment.maxX + 32, y: appointment.midY - 16))
    label("has", CGPoint(x: 1378, y: appointment.midY - 36))

    polyline([CGPoint(x: appointment.maxX, y: appointment.midY + 48), CGPoint(x: surgery.minX, y: appointment.midY + 48), CGPoint(x: surgery.minX, y: surgery.midY)])
    label("0..*", CGPoint(x: appointment.maxX + 32, y: appointment.midY + 30))
    label("1", CGPoint(x: surgery.minX - 24, y: surgery.midY - 16))
    label("scheduledAt", CGPoint(x: 1400, y: appointment.midY + 12))

    polyline([CGPoint(x: patient.maxX, y: patient.midY), CGPoint(x: patientAddress.minX, y: patientAddress.midY)])
    label("1", CGPoint(x: patient.maxX + 28, y: patient.midY - 16))
    label("1", CGPoint(x: patientAddress.minX - 24, y: patientAddress.midY - 16))
    label("mailing", CGPoint(x: 1660, y: patient.midY - 36))

    polyline([CGPoint(x: surgery.maxX, y: surgery.midY), CGPoint(x: surgeryAddress.minX, y: surgeryAddress.midY)])
    label("1", CGPoint(x: surgery.maxX + 28, y: surgery.midY - 16))
    label("1", CGPoint(x: surgeryAddress.minX - 24, y: surgeryAddress.midY - 16))
    label("location", CGPoint(x: 1662, y: surgery.midY - 36))

    polyline([CGPoint(x: patient.midX + 70, y: patient.maxY), CGPoint(x: patient.midX + 70, y: bill.midY), CGPoint(x: bill.maxX, y: bill.midY)])
    label("1", CGPoint(x: patient.midX + 100, y: patient.maxY + 24))
    label("0..*", CGPoint(x: bill.maxX + 34, y: bill.midY - 16))
    label("owes", CGPoint(x: 1665, y: 1002))

    polyline([CGPoint(x: appointment.midX + 180, y: appointment.maxY), CGPoint(x: appointment.midX + 180, y: bill.minY), CGPoint(x: bill.minX, y: bill.minY)])
    label("0..1", CGPoint(x: appointment.midX + 214, y: appointment.maxY + 24))
    label("0..1", CGPoint(x: bill.minX - 36, y: bill.minY + 20))
    label("generates", CGPoint(x: 1210, y: 968))

    polyline([CGPoint(x: apptStatus.maxX, y: apptStatus.midY), CGPoint(x: appointment.minX, y: appointment.midY + 78)])
    polyline([CGPoint(x: billStatus.minX, y: billStatus.midY), CGPoint(x: bill.maxX, y: bill.midY + 18)])
}

try savePNG(width: 2300, height: 1500, filename: "database-er-diagram.png") { _ in
    text("BrightSmile ADS Relational E-R Model", NSRect(x: 0, y: 30, width: 2300, height: 52),
         size: 38, weight: .bold, color: ink, align: .center)
    text("Course Project relational database design for Dental Surgery Appointment webapp",
         NSRect(x: 0, y: 82, width: 2300, height: 34), size: 20, color: muted, align: .center)

    let role = NSRect(x: 985, y: 150, width: 330, height: 132)
    let user = NSRect(x: 545, y: 380, width: 390, height: 220)
    let dentist = NSRect(x: 130, y: 740, width: 430, height: 198)
    let patient = NSRect(x: 1690, y: 380, width: 430, height: 244)
    let surgery = NSRect(x: 130, y: 1070, width: 430, height: 170)
    let appointment = NSRect(x: 820, y: 775, width: 620, height: 270)
    let bill = NSRect(x: 1655, y: 980, width: 430, height: 190)
    let address = NSRect(x: 1690, y: 720, width: 430, height: 172)

    tableBox("ROLES", fields: [("PK", "role_id"), ("", "role_name UNIQUE"), ("", "description")], at: role)
    tableBox("APP_USERS", fields: [("PK", "user_id"), ("FK", "role_id"), ("", "username UNIQUE"), ("", "password"), ("", "first_name"), ("", "last_name"), ("", "email UNIQUE")], at: user)
    tableBox("DENTISTS", fields: [("PK", "dentist_id"), ("", "dentist_code UNIQUE"), ("", "first_name"), ("", "last_name"), ("", "phone_number"), ("", "email UNIQUE"), ("", "specialization")], at: dentist)
    tableBox("PATIENTS", fields: [("PK", "patient_id"), ("FK", "address_id"), ("", "patient_number UNIQUE"), ("", "first_name"), ("", "last_name"), ("", "phone_number"), ("", "email UNIQUE"), ("", "date_of_birth")], at: patient)
    tableBox("SURGERIES", fields: [("PK", "surgery_id"), ("FK", "address_id"), ("", "surgery_number UNIQUE"), ("", "name"), ("", "phone_number")], at: surgery)
    tableBox("APPOINTMENTS", fields: [("PK", "appointment_id"), ("FK", "patient_id"), ("FK", "dentist_id"), ("FK", "surgery_id"), ("FK", "booked_by_user_id"), ("", "appointment_date"), ("", "appointment_time"), ("", "status"), ("", "confirmation_sent"), ("", "reason")], at: appointment)
    tableBox("DENTAL_BILLS", fields: [("PK", "bill_id"), ("FK", "patient_id"), ("FK", "appointment_id UNIQUE"), ("", "issue_date"), ("", "amount"), ("", "status"), ("", "description")], at: bill)
    tableBox("ADDRESSES", fields: [("PK", "address_id"), ("", "street"), ("", "city"), ("", "state"), ("", "zip_code")], at: address)

    polyline([CGPoint(x: role.midX, y: role.maxY), CGPoint(x: role.midX, y: 330), CGPoint(x: user.midX, y: 330), CGPoint(x: user.midX, y: user.minY)])
    label("1", CGPoint(x: role.midX - 32, y: role.maxY + 22))
    label("0..*", CGPoint(x: user.midX + 40, y: user.minY - 18))

    polyline([CGPoint(x: user.midX, y: user.maxY), CGPoint(x: user.midX, y: appointment.midY), CGPoint(x: appointment.minX, y: appointment.midY)])
    label("1", CGPoint(x: user.midX - 34, y: user.maxY + 24))
    label("0..*", CGPoint(x: appointment.minX - 32, y: appointment.midY - 18))

    polyline([CGPoint(x: dentist.maxX, y: dentist.midY), CGPoint(x: appointment.minX, y: dentist.midY), CGPoint(x: appointment.minX, y: appointment.midY - 52)])
    label("1", CGPoint(x: dentist.maxX + 26, y: dentist.midY - 14))
    label("0..*", CGPoint(x: appointment.minX + 35, y: appointment.midY - 72))

    polyline([CGPoint(x: surgery.maxX, y: surgery.midY), CGPoint(x: appointment.minX, y: surgery.midY), CGPoint(x: appointment.minX, y: appointment.midY + 72)])
    label("1", CGPoint(x: surgery.maxX + 26, y: surgery.midY - 14))
    label("0..*", CGPoint(x: appointment.minX + 35, y: appointment.midY + 52))

    polyline([CGPoint(x: patient.minX, y: patient.midY), CGPoint(x: appointment.maxX, y: patient.midY), CGPoint(x: appointment.maxX, y: appointment.midY - 24)])
    label("1", CGPoint(x: patient.minX - 26, y: patient.midY - 14))
    label("0..*", CGPoint(x: appointment.maxX + 35, y: appointment.midY - 42))

    polyline([CGPoint(x: patient.midX, y: patient.maxY), CGPoint(x: patient.midX, y: bill.minY)])
    label("1", CGPoint(x: patient.midX - 32, y: patient.maxY + 22))
    label("0..*", CGPoint(x: patient.midX + 36, y: bill.minY - 16))

    polyline([CGPoint(x: appointment.maxX, y: appointment.midY + 62), CGPoint(x: bill.minX, y: appointment.midY + 62), CGPoint(x: bill.minX, y: bill.midY)])
    label("0..1", CGPoint(x: appointment.maxX + 40, y: appointment.midY + 42))
    label("0..1", CGPoint(x: bill.minX - 38, y: bill.midY - 18))

    polyline([CGPoint(x: patient.midX, y: patient.maxY + 4), CGPoint(x: patient.midX, y: address.minY)])
    label("1", CGPoint(x: patient.midX - 30, y: patient.maxY + 28))
    label("1", CGPoint(x: address.midX + 34, y: address.minY - 16))

    polyline([CGPoint(x: surgery.maxX, y: surgery.midY + 46), CGPoint(x: address.minX, y: surgery.midY + 46), CGPoint(x: address.minX, y: address.midY)])
    label("0..*", CGPoint(x: surgery.maxX + 40, y: surgery.midY + 26))
    label("1", CGPoint(x: address.minX - 24, y: address.midY - 18))

    text("Business constraints: unique dentist/date/time slot, unpaid bill blocks new appointment, max 5 active appointments per dentist per week.",
         NSRect(x: 170, y: 1340, width: 1960, height: 42), size: 20, weight: .medium, color: muted, align: .center)
}

try savePNG(width: 2600, height: 1700, filename: "solution-architecture.png") { _ in
    text("BrightSmile ADS Software Solution Architecture", NSRect(x: 0, y: 28, width: 2600, height: 54),
         size: 40, weight: .bold, color: ink, align: .center)
    text("Chosen style: 3-tier layered modular monolith | Tech stack: Browser UI + Spring Boot + MySQL 8.4 + Docker Compose on AWS EC2",
         NSRect(x: 0, y: 82, width: 2600, height: 34), size: 22, color: muted, align: .center)

    text("Physical Tiers", NSRect(x: 115, y: 150, width: 300, height: 34), size: 28, weight: .bold, color: ink)

    rounded(NSRect(x: 70, y: 215, width: 330, height: 260), fill: NSColor.white, stroke: blue, width: 2.5, radius: 12, dash: [13, 10])
    text("Client Tier", NSRect(x: 70, y: 248, width: 330, height: 32), size: 24, weight: .bold, color: ink, align: .center)
    rounded(NSRect(x: 128, y: 315, width: 214, height: 86), fill: paleBlue, stroke: blue, width: 2, radius: 12)
    text("Browser", NSRect(x: 128, y: 337, width: 214, height: 28), size: 26, weight: .bold, color: ink, align: .center)
    text("HTML/CSS/JS UI", NSRect(x: 128, y: 370, width: 214, height: 22), size: 17, color: muted, align: .center)

    rounded(NSRect(x: 70, y: 540, width: 330, height: 390), fill: NSColor.white, stroke: blue, width: 2.5, radius: 12, dash: [13, 10])
    text("Middle Tier", NSRect(x: 70, y: 575, width: 330, height: 32), size: 24, weight: .bold, color: ink, align: .center)
    rounded(NSRect(x: 120, y: 660, width: 230, height: 150), fill: paleBlue, stroke: blue, width: 2, radius: 12)
    text("EC2 Docker Host", NSRect(x: 120, y: 682, width: 230, height: 32), size: 22, weight: .bold, color: ink, align: .center)
    text("Spring Boot app\nREST API\nBusiness services", NSRect(x: 120, y: 725, width: 230, height: 74), size: 17, color: muted, align: .center)

    rounded(NSRect(x: 70, y: 1030, width: 330, height: 300), fill: NSColor.white, stroke: blue, width: 2.5, radius: 12, dash: [13, 10])
    text("Data Tier", NSRect(x: 70, y: 1066, width: 330, height: 32), size: 24, weight: .bold, color: ink, align: .center)
    rounded(NSRect(x: 130, y: 1162, width: 210, height: 88), fill: paleBlue, stroke: blue, width: 2, radius: 12)
    text("MySQL 8.4", NSRect(x: 130, y: 1185, width: 210, height: 28), size: 24, weight: .bold, color: ink, align: .center)
    text("Docker volume", NSRect(x: 130, y: 1218, width: 210, height: 22), size: 17, color: muted, align: .center)

    rounded(NSRect(x: 480, y: 215, width: 1980, height: 1350), fill: NSColor.white, stroke: blue, width: 2.5, radius: 14)
    text("Logical Tiers and Layers", NSRect(x: 480, y: 145, width: 1980, height: 42), size: 28, weight: .bold, color: ink, align: .center)

    rounded(NSRect(x: 560, y: 285, width: 520, height: 250), fill: paleBlue, stroke: blue, width: 2, radius: 12)
    text("Client Application", NSRect(x: 560, y: 316, width: 520, height: 34), size: 26, weight: .bold, color: ink, align: .center)
    line(CGPoint(x: 560, y: 370), CGPoint(x: 1080, y: 370))
    text("Single-page web console\nLogin, dashboard, patients, appointments\nFetch API + JSON DTO payloads",
         NSRect(x: 590, y: 404, width: 460, height: 92), size: 20, color: ink, align: .center)

    rounded(NSRect(x: 1255, y: 255, width: 1070, height: 1010), fill: NSColor(calibratedRed: 250/255, green: 253/255, blue: 255/255, alpha: 1), stroke: blue, width: 2.5, radius: 14)
    text("Application Server / Layered Backend", NSRect(x: 1255, y: 288, width: 1070, height: 34), size: 27, weight: .bold, color: ink, align: .center)

    rounded(NSRect(x: 1360, y: 360, width: 860, height: 92), fill: paleBlue, stroke: blue, width: 2, radius: 12)
    text("Security and Access Control", NSRect(x: 1360, y: 380, width: 860, height: 30), size: 23, weight: .bold, color: ink, align: .center)
    text("Spring Security, JWT authentication, role-based authorization", NSRect(x: 1360, y: 415, width: 860, height: 25), size: 18, color: muted, align: .center)

    rounded(NSRect(x: 1360, y: 510, width: 860, height: 112), fill: paleBlue, stroke: blue, width: 2, radius: 12)
    text("Presentation / API Layer", NSRect(x: 1360, y: 536, width: 860, height: 30), size: 23, weight: .bold, color: ink, align: .center)
    text("Spring MVC REST controllers, validation, DTO mapping", NSRect(x: 1360, y: 572, width: 860, height: 25), size: 18, color: muted, align: .center)

    rounded(NSRect(x: 1360, y: 680, width: 860, height: 330), fill: paleBlue, stroke: blue, width: 2, radius: 12)
    text("Business Service Layer", NSRect(x: 1360, y: 708, width: 860, height: 30), size: 23, weight: .bold, color: ink, align: .center)
    let serviceBoxes = [
        ("Patient\nManagement Service", 1410, 770),
        ("Appointment\nScheduling Service", 1710, 770),
        ("Directory\nDentist/Surgery Service", 1410, 890),
        ("Billing Rule\nOutstanding Balance Check", 1710, 890)
    ]
    for (v, x, y) in serviceBoxes {
        rounded(NSRect(x: x, y: y, width: 250, height: 78), fill: NSColor.white, stroke: blue, width: 2, radius: 10)
        text(v, NSRect(x: x + 12, y: y + 16, width: 226, height: 50), size: 18, weight: .bold, color: ink, align: .center)
    }

    rounded(NSRect(x: 1360, y: 1060, width: 860, height: 95), fill: paleBlue, stroke: blue, width: 2, radius: 12)
    text("Persistence Layer", NSRect(x: 1360, y: 1080, width: 860, height: 30), size: 23, weight: .bold, color: ink, align: .center)
    text("Spring Data JPA repositories and transaction management", NSRect(x: 1360, y: 1116, width: 860, height: 25), size: 18, color: muted, align: .center)

    rounded(NSRect(x: 1560, y: 1205, width: 455, height: 62), fill: NSColor.white, stroke: blue, width: 2, radius: 12)
    text("Hibernate ORM", NSRect(x: 1560, y: 1223, width: 455, height: 30), size: 23, weight: .bold, color: ink, align: .center)

    rounded(NSRect(x: 1255, y: 1340, width: 1070, height: 130), fill: paleBlue, stroke: blue, width: 2, radius: 12)
    text("Data and Deployment Services", NSRect(x: 1255, y: 1366, width: 1070, height: 30), size: 23, weight: .bold, color: ink, align: .center)
    rounded(NSRect(x: 1530, y: 1415, width: 520, height: 58), fill: NSColor.white, stroke: blue, width: 2, radius: 10)
    text("MySQL Database Container on AWS EC2", NSRect(x: 1530, y: 1430, width: 520, height: 28), size: 22, weight: .bold, color: ink, align: .center)

    rounded(NSRect(x: 560, y: 600, width: 520, height: 150), fill: paleGold, stroke: gold, width: 2, radius: 12)
    text("Architecture Choice", NSRect(x: 560, y: 625, width: 520, height: 30), size: 24, weight: .bold, color: ink, align: .center)
    text("3-tier web architecture\nLayered modular monolith\nContainerized for cloud demo",
         NSRect(x: 560, y: 668, width: 520, height: 74), size: 20, color: ink, align: .center)

    arrow(CGPoint(x: 342, y: 358), CGPoint(x: 560, y: 358))
    text("HTTPS / JSON", NSRect(x: 1090, y: 360, width: 160, height: 25), size: 16, color: muted, align: .center)
    arrow(CGPoint(x: 1080, y: 410), CGPoint(x: 1360, y: 565))
    arrow(CGPoint(x: 350, y: 735), CGPoint(x: 1255, y: 735))
    arrow(CGPoint(x: 340, y: 1206), CGPoint(x: 1530, y: 1445))
    arrow(CGPoint(x: 1788, y: 1267), CGPoint(x: 1788, y: 1415))
    text("JPA / SQL", NSRect(x: 1805, y: 1305, width: 120, height: 24), size: 16, color: muted)
}

// Final presentation-friendly overrides with more whitespace for printed/zoomed review.
try savePNG(width: 2600, height: 1600, filename: "domain-model-class-diagram.png") { _ in
    text("BrightSmile Dental Surgery Appointment Domain Model", NSRect(x: 0, y: 34, width: 2600, height: 52),
         size: 38, weight: .bold, color: ink, align: .center)
    text("Course Project static model: people, appointments, surgeries, billing, and security roles",
         NSRect(x: 0, y: 84, width: 2600, height: 34), size: 20, color: muted, align: .center)

    let person = NSRect(x: 935, y: 145, width: 730, height: 138)
    let role = NSRect(x: 105, y: 380, width: 310, height: 118)
    let appUser = NSRect(x: 525, y: 380, width: 410, height: 148)
    let dentist = NSRect(x: 1095, y: 380, width: 410, height: 148)
    let patient = NSRect(x: 1660, y: 380, width: 450, height: 166)
    let patientAddress = NSRect(x: 2175, y: 386, width: 360, height: 145)
    let appointment = NSRect(x: 820, y: 750, width: 750, height: 212)
    let surgery = NSRect(x: 1775, y: 765, width: 445, height: 148)
    let surgeryAddress = NSRect(x: 2175, y: 1015, width: 360, height: 145)
    let bill = NSRect(x: 1660, y: 1210, width: 450, height: 148)
    let appointmentStatus = NSRect(x: 310, y: 1035, width: 360, height: 138)
    let billStatus = NSRect(x: 2175, y: 1250, width: 300, height: 112)

    classBox("Person", fields: ["firstName : String", "lastName : String", "phoneNumber : String", "email : String"], at: person)
    classBox("Role", fields: ["roleId : Long", "roleName : String"], at: role, titleSize: 20)
    classBox("AppUser", fields: ["userId : Long", "username : String", "password : String"], at: appUser)
    classBox("Dentist", fields: ["dentistId : Long", "dentistCode : String", "specialization : String"], at: dentist)
    classBox("Patient", fields: ["patientId : Long", "patientNumber : String", "dateOfBirth : LocalDate"], at: patient)
    classBox("Address", fields: ["addressId : Long", "street : String", "city : String", "state : String", "zipCode : String"], at: patientAddress)
    classBox("Appointment", fields: ["appointmentId : Long", "appointmentDate : LocalDate", "appointmentTime : LocalTime", "status : AppointmentStatus", "confirmationSent : boolean", "reason : String"], at: appointment)
    classBox("Surgery", fields: ["surgeryId : Long", "surgeryNumber : String", "name : String", "phoneNumber : String"], at: surgery)
    classBox("Address", fields: ["addressId : Long", "street : String", "city : String", "state : String", "zipCode : String"], at: surgeryAddress)
    classBox("DentalBill", fields: ["billId : Long", "issueDate : LocalDate", "amount : BigDecimal", "status : BillStatus"], at: bill)
    classBox("AppointmentStatus", fields: ["SCHEDULED", "COMPLETED", "CANCELLED"], at: appointmentStatus, titleSize: 19)
    classBox("BillStatus", fields: ["PAID", "UNPAID", "VOID"], at: billStatus, titleSize: 19)

    inheritance(CGPoint(x: appUser.midX, y: appUser.minY), CGPoint(x: person.minX + 130, y: person.maxY))
    inheritance(CGPoint(x: dentist.midX, y: dentist.minY), CGPoint(x: person.midX, y: person.maxY))
    inheritance(CGPoint(x: patient.midX, y: patient.minY), CGPoint(x: person.maxX - 130, y: person.maxY))

    polyline([CGPoint(x: role.maxX, y: role.midY), CGPoint(x: appUser.minX, y: appUser.midY)])
    label("1", CGPoint(x: role.maxX + 22, y: role.midY - 16))
    label("0..*", CGPoint(x: appUser.minX - 26, y: appUser.midY - 16))
    label("assigned", CGPoint(x: 470, y: role.midY - 34), size: 15)

    polyline([CGPoint(x: appUser.midX, y: appUser.maxY), CGPoint(x: appUser.midX, y: appointment.midY), CGPoint(x: appointment.minX, y: appointment.midY)])
    label("1", CGPoint(x: appUser.midX, y: appUser.maxY + 24))
    label("0..*", CGPoint(x: appointment.minX - 30, y: appointment.midY - 18))
    label("books", CGPoint(x: 720, y: appointment.midY - 34))

    polyline([CGPoint(x: dentist.midX, y: dentist.maxY), CGPoint(x: dentist.midX, y: appointment.minY)])
    label("1", CGPoint(x: dentist.midX - 30, y: dentist.maxY + 24))
    label("0..*", CGPoint(x: dentist.midX - 34, y: appointment.minY - 16))
    label("treats", CGPoint(x: dentist.midX + 55, y: 645))

    polyline([CGPoint(x: patient.midX, y: patient.maxY), CGPoint(x: patient.midX, y: appointment.midY), CGPoint(x: appointment.maxX, y: appointment.midY)])
    label("1", CGPoint(x: patient.midX + 30, y: patient.maxY + 24))
    label("0..*", CGPoint(x: appointment.maxX + 34, y: appointment.midY - 18))
    label("has", CGPoint(x: 1618, y: appointment.midY - 36))

    polyline([CGPoint(x: patient.maxX, y: patient.midY), CGPoint(x: patientAddress.minX, y: patientAddress.midY)])
    label("1", CGPoint(x: patient.maxX + 26, y: patient.midY - 16))
    label("1", CGPoint(x: patientAddress.minX - 24, y: patientAddress.midY - 16))
    label("mailing", CGPoint(x: 2146, y: patient.midY - 36))

    polyline([CGPoint(x: appointment.maxX, y: appointment.midY + 58), CGPoint(x: surgery.minX, y: appointment.midY + 58), CGPoint(x: surgery.minX, y: surgery.midY)])
    label("0..*", CGPoint(x: appointment.maxX + 34, y: appointment.midY + 38))
    label("1", CGPoint(x: surgery.minX - 26, y: surgery.midY - 16))
    label("scheduledAt", CGPoint(x: 1690, y: appointment.midY + 22))

    polyline([CGPoint(x: surgery.maxX, y: surgery.midY), CGPoint(x: surgeryAddress.minX, y: surgeryAddress.midY)])
    label("1", CGPoint(x: surgery.maxX + 24, y: surgery.midY - 16))
    label("1", CGPoint(x: surgeryAddress.minX - 24, y: surgeryAddress.midY - 16))
    label("location", CGPoint(x: 2192, y: surgery.midY - 36))

    polyline([CGPoint(x: patient.midX + 80, y: patient.maxY), CGPoint(x: patient.midX + 80, y: bill.minY), CGPoint(x: bill.midX, y: bill.minY)])
    label("1", CGPoint(x: patient.midX + 112, y: patient.maxY + 24))
    label("0..*", CGPoint(x: bill.midX + 38, y: bill.minY - 16))
    label("owes", CGPoint(x: patient.midX + 132, y: 1000))

    polyline([CGPoint(x: appointment.midX + 210, y: appointment.maxY), CGPoint(x: appointment.midX + 210, y: bill.midY), CGPoint(x: bill.minX, y: bill.midY)])
    label("0..1", CGPoint(x: appointment.midX + 246, y: appointment.maxY + 24))
    label("0..1", CGPoint(x: bill.minX - 36, y: bill.midY - 16))
    label("generates", CGPoint(x: 1504, y: 1168))

    polyline([CGPoint(x: appointmentStatus.maxX, y: appointmentStatus.midY), CGPoint(x: appointment.minX, y: appointment.midY + 78)])
    polyline([CGPoint(x: bill.maxX, y: bill.midY + 20), CGPoint(x: billStatus.minX, y: billStatus.midY)])
}

try savePNG(width: 2600, height: 1600, filename: "database-er-diagram.png") { _ in
    text("BrightSmile ADS Relational E-R Model", NSRect(x: 0, y: 30, width: 2600, height: 52),
         size: 38, weight: .bold, color: ink, align: .center)
    text("Course Project relational database design for Dental Surgery Appointment webapp",
         NSRect(x: 0, y: 82, width: 2600, height: 34), size: 20, color: muted, align: .center)

    let role = NSRect(x: 1115, y: 150, width: 370, height: 124)
    let user = NSRect(x: 585, y: 360, width: 440, height: 205)
    let patient = NSRect(x: 1845, y: 360, width: 460, height: 224)
    let address = NSRect(x: 1845, y: 680, width: 460, height: 164)
    let dentist = NSRect(x: 135, y: 710, width: 480, height: 188)
    let surgery = NSRect(x: 135, y: 1035, width: 480, height: 168)
    let appointment = NSRect(x: 850, y: 720, width: 690, height: 292)
    let bill = NSRect(x: 1845, y: 1035, width: 460, height: 184)

    tableBox("ROLES", fields: [("PK", "role_id"), ("", "role_name UNIQUE"), ("", "description")], at: role)
    tableBox("APP_USERS", fields: [("PK", "user_id"), ("FK", "role_id"), ("", "username UNIQUE"), ("", "password"), ("", "first_name"), ("", "last_name"), ("", "email UNIQUE")], at: user)
    tableBox("PATIENTS", fields: [("PK", "patient_id"), ("FK", "address_id"), ("", "patient_number UNIQUE"), ("", "first_name"), ("", "last_name"), ("", "phone_number"), ("", "email UNIQUE"), ("", "date_of_birth")], at: patient)
    tableBox("ADDRESSES", fields: [("PK", "address_id"), ("", "street"), ("", "city"), ("", "state"), ("", "zip_code")], at: address)
    tableBox("DENTISTS", fields: [("PK", "dentist_id"), ("", "dentist_code UNIQUE"), ("", "first_name"), ("", "last_name"), ("", "phone_number"), ("", "email UNIQUE"), ("", "specialization")], at: dentist)
    tableBox("SURGERIES", fields: [("PK", "surgery_id"), ("FK", "address_id"), ("", "surgery_number UNIQUE"), ("", "name"), ("", "phone_number")], at: surgery)
    tableBox("APPOINTMENTS", fields: [("PK", "appointment_id"), ("FK", "patient_id"), ("FK", "dentist_id"), ("FK", "surgery_id"), ("FK", "booked_by_user_id"), ("", "appointment_date"), ("", "appointment_time"), ("", "status"), ("", "confirmation_sent"), ("", "reason")], at: appointment)
    tableBox("DENTAL_BILLS", fields: [("PK", "bill_id"), ("FK", "patient_id"), ("FK", "appointment_id UNIQUE"), ("", "issue_date"), ("", "amount"), ("", "status"), ("", "description")], at: bill)

    polyline([CGPoint(x: role.midX, y: role.maxY), CGPoint(x: role.midX, y: 320), CGPoint(x: user.midX, y: 320), CGPoint(x: user.midX, y: user.minY)])
    label("1", CGPoint(x: role.midX - 30, y: role.maxY + 20))
    label("0..*", CGPoint(x: user.midX + 38, y: user.minY - 16))

    polyline([CGPoint(x: user.midX, y: user.maxY), CGPoint(x: user.midX, y: appointment.midY), CGPoint(x: appointment.minX, y: appointment.midY)])
    label("1", CGPoint(x: user.midX - 32, y: user.maxY + 22))
    label("0..*", CGPoint(x: appointment.minX - 32, y: appointment.midY - 16))

    polyline([CGPoint(x: dentist.maxX, y: dentist.midY), CGPoint(x: appointment.minX, y: dentist.midY), CGPoint(x: appointment.minX, y: appointment.midY - 58)])
    label("1", CGPoint(x: dentist.maxX + 26, y: dentist.midY - 14))
    label("0..*", CGPoint(x: appointment.minX + 36, y: appointment.midY - 76))

    polyline([CGPoint(x: surgery.maxX, y: surgery.midY), CGPoint(x: appointment.minX, y: surgery.midY), CGPoint(x: appointment.minX, y: appointment.midY + 78)])
    label("1", CGPoint(x: surgery.maxX + 26, y: surgery.midY - 14))
    label("0..*", CGPoint(x: appointment.minX + 36, y: appointment.midY + 58))

    polyline([CGPoint(x: patient.minX, y: patient.midY), CGPoint(x: appointment.maxX, y: patient.midY), CGPoint(x: appointment.maxX, y: appointment.midY - 28)])
    label("1", CGPoint(x: patient.minX - 26, y: patient.midY - 14))
    label("0..*", CGPoint(x: appointment.maxX + 34, y: appointment.midY - 48))

    polyline([CGPoint(x: patient.midX, y: patient.maxY), CGPoint(x: patient.midX, y: bill.minY)])
    label("1", CGPoint(x: patient.midX - 32, y: patient.maxY + 20))
    label("0..*", CGPoint(x: patient.midX + 36, y: bill.minY - 16))

    polyline([CGPoint(x: appointment.maxX, y: appointment.midY + 72), CGPoint(x: bill.minX, y: appointment.midY + 72), CGPoint(x: bill.minX, y: bill.midY)])
    label("0..1", CGPoint(x: appointment.maxX + 42, y: appointment.midY + 52))
    label("0..1", CGPoint(x: bill.minX - 38, y: bill.midY - 18))

    polyline([CGPoint(x: patient.midX, y: patient.maxY + 2), CGPoint(x: patient.midX, y: address.minY)])
    label("1", CGPoint(x: patient.midX + 34, y: patient.maxY + 22))
    label("1", CGPoint(x: address.midX + 34, y: address.minY - 16))

    polyline([CGPoint(x: surgery.maxX, y: surgery.midY + 44), CGPoint(x: address.minX, y: surgery.midY + 44), CGPoint(x: address.minX, y: address.midY)])
    label("0..*", CGPoint(x: surgery.maxX + 42, y: surgery.midY + 24))
    label("1", CGPoint(x: address.minX - 24, y: address.midY - 16))

    text("Business constraints: unique dentist/date/time slot, unpaid bill blocks new appointment, max 5 active appointments per dentist per week.",
         NSRect(x: 180, y: 1410, width: 2240, height: 42), size: 20, weight: .medium, color: muted, align: .center)
}

print("Generated diagrams in \(outDir.path)")
