class A { }

class B : A { }

A create_b() {
	return new B();
}

int main(string [] args) {
	A a = new B();
	var inferred = create_b() as B;
	var c = infer<caret>red;
}