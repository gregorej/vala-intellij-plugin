public GLib.Bytes slice (int start, int end) {
	unowned uint8[] data = {1,2,3,4,5,6}
	var slice = data[3:5];
	return new GLib.Bytes (data[start:end]);
}