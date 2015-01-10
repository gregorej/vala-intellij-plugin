void validateName(string name) {
}

void update(User user) {
}

int updateAmount(string name, int amount) {
	validateName(name);
	var user = findByName(nam<caret>e);
	update(name, user);
}