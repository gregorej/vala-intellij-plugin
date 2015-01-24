bool someCondition() {
	return false;
}

int main(string [] args) {
	bool flag = true;
	int k = 5;

	if (flag && someCondition() && k > 3 || k <2) {
		return 1;
	}
	return 0;
}