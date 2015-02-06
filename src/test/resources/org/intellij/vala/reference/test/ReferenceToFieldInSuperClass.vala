class Parent {
	int count;
}

class Child: Parent {

}

int main(string [] args) {
	Child c = new Child();
	c.co<caret>unt = 22;
	return 0;
}
