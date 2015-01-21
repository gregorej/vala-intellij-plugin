class Person {
	Address getAddress() {
	}
}

class Address {
	string getName() {
		return "some city";
	}
}

int main(string [] args) {
	Person p = new Person();
	print(p.getAddress().getNa<caret>me());
}