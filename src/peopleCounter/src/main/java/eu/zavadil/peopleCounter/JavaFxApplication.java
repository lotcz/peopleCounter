package eu.zavadil.peopleCounter;

import eu.zavadil.peopleCounter.ui.ApplicationReadyEvent;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;

public class JavaFxApplication extends Application {

	private ConfigurableApplicationContext context;


	@Override
	public void init() {

		ApplicationContextInitializer<GenericApplicationContext> initializer =
				ac -> {
					ac.registerBean(Application.class, () -> JavaFxApplication.this);
					ac.registerBean(Parameters.class, () -> getParameters());
					ac.registerBean(HostServices.class, () -> getHostServices());
				};

		this.context = new SpringApplicationBuilder()
				.sources(MySpringApplication.class)
				.initializers(initializer)
				.run(getParameters().getRaw().toArray(new String[0]));
	}

	@Override
	public void start(Stage stage) {
		this.context.publishEvent(new ApplicationReadyEvent(this, stage));
	}

	@Override
	public void stop() {
		System.out.println("JavaFxApplication: closing application.");
		context.close();
		Platform.exit();
	}

	public static void launchApp(String[] args) {
		JavaFxApplication.launch(args);
	}

}
