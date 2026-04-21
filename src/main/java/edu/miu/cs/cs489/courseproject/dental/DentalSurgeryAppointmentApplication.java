package edu.miu.cs.cs489.courseproject.dental;

import edu.miu.cs.cs489.courseproject.dental.service.ClinicSeedService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class DentalSurgeryAppointmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(DentalSurgeryAppointmentApplication.class, args);
    }

    @Component
    static class DemoDataRunner implements ApplicationRunner {

        private final ClinicSeedService clinicSeedService;
        private final boolean seedEnabled;

        DemoDataRunner(ClinicSeedService clinicSeedService,
                       @Value("${app.seed.enabled:true}") boolean seedEnabled) {
            this.clinicSeedService = clinicSeedService;
            this.seedEnabled = seedEnabled;
        }

        @Override
        public void run(ApplicationArguments args) {
            if (seedEnabled) {
                clinicSeedService.seedDemoData();
            }
        }
    }
}
