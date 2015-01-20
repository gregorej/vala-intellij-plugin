class File {

	void open () {
	}

	void close () {
	}
}

int main(string [] args) {
	File f = new File();
	f.op<caret>en();
	f.close();
	return 0;
}