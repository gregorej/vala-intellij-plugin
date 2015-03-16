public enum EnumWithMethodsWithAttributes {
		UNDEFINED,
		DEFAULT,
		BYTES,
		TIME,
		BUFFERS,
		PERCENT;
		[CCode (cname = "gst_formats_contains")]
		public static bool contains (string val);
		public string as_string();
}