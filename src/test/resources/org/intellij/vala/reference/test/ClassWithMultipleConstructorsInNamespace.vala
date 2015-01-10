namespace SomeNamespace {

	/* some comment to check if it still breaks parsing in editor */

	public class ClassWithMultipleConstructors {
		public ClassWithMultipleConstructors () { }
		public ClassWithMultipleConstructors.with_foo (int foo) { }
		public ClassWithMultipleConstructors.from_bar (string bar) { }
	}

}