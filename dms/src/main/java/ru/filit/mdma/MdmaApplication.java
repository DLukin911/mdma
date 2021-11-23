package ru.filit.mdma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class MdmaApplication {

  public MdmaApplication() {
  }

  @SpringBootApplication(
      scanBasePackages = {"ru.filit.mdma"}
  )
  public class ApplicationConfig extends SpringBootServletInitializer {

    public ApplicationConfig() {
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(new Class[]{ApplicationConfig.class});
    }

    public static void main(String[] args) {
      SpringApplication.run(ApplicationConfig.class, args);
    }
  }
}