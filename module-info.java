module TwoDTest {
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.annotation;
	requires transitive javafx.controls;
	exports application;
	exports misc;
	
	opens application to javafx.graphics, javafx.fxml;
}
