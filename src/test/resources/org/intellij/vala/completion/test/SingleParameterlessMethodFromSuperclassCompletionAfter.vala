class A {
	int some_method();
}

class B: A {

}

int main(string [] args) {
	var obj = new B();
	obj.some_method()<caret>
}