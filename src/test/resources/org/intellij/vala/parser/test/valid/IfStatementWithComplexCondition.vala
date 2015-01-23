bool someCondition() {
	return true;
}

int main(string [] args) {
	int l = 4;
	bool flag = true;
	if (flag || someCondition() || l > 3) {
		int k = 5;
		if (k > 5 && l < 10 && someCondition()) {
			return 1;
		}
	}
	return 0;
}