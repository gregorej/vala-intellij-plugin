interface MyInterface {
	int method();
}

void callMethod(MyInterface inter) {
	inter.met<caret>hod();
}