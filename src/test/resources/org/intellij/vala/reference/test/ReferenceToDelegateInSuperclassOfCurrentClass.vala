class Parent {
	public void methodInParent();
}

class Child : Parent {
	public void childMethod() {
		method<caret>InParent();
	}
}