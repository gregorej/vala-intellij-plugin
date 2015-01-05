class WithMethodReference {

    public WithMethodReference () { }

	public int method1 () { }

	public int method2 () {
		metho<caret>d1 ();
		return 0;
	}
}