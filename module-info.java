module TwoDTest {
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.annotation;
	requires javafx.controls;
	
	opens application to javafx.graphics, javafx.fxml;
}
