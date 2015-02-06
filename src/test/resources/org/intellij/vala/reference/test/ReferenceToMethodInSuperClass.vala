class Parent {
	int getCount() {
		return 0;
	}
}

class Child: Parent {

}

int main(string [] args) {
	Child obj = new Child();
	return obj.get<caret>Count();
}