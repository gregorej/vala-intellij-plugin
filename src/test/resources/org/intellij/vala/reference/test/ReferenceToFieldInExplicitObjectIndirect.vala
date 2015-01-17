
class MyNode {
	int value;
	MyNode next;
}

int main(string [] args) {
	MyNode head = new MyNode();
	head.next = new MyNode();
	head.next.ne<caret>xt = new MyNode();
	head.next.next.value = 13;
	return head.value;
}