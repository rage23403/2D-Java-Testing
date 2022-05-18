module TwoDTest {
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.annotation;
	requires transitive javafx.controls;
	exports application; //required to use JSONComp's "load" function on classes in application.
	exports misc; //recommended to use any of misc's files/functions inside the application project. removal only causes warnings
	
	opens application to javafx.graphics, javafx.fxml;
}
