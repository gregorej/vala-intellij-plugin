namespace Something {

[CCode (cheader_filename = "gst/gst.h", ref_function = "gst_date_time_ref", type_id = "gst_date_time_get_type ()", unref_function = "gst_date_time_unref")]
	[Compact]
	public class DateTime {
		[CCode (has_construct_function = false)]
		public DateTime (float tzoffset, int year, int month, int day, int hour, int minute, double seconds);
		[CCode (has_construct_function = false)]
		public DateTime.from_g_date_time (owned GLib.DateTime dt);
		[CCode (has_construct_function = false)]
		public DateTime.from_iso8601_string (string @string);
		[CCode (has_construct_function = false)]
		public DateTime.from_unix_epoch_local_time (int64 secs);
		[CCode (has_construct_function = false)]
		public DateTime.from_unix_epoch_utc (int64 secs);
		public int get_day ();
		public int get_hour ();
		public int get_microsecond ();
		public int get_minute ();
		public int get_month ();
		public int get_second ();
		public float get_time_zone_offset ();
		public int get_year ();
		public bool has_day ();
		public bool has_month ();
		public bool has_second ();
		public bool has_time ();
		public bool has_year ();
		[CCode (has_construct_function = false)]
		public DateTime.local_time (int year, int month, int day, int hour, int minute, double seconds);
		[CCode (has_construct_function = false)]
		public DateTime.now_local_time ();
		[CCode (has_construct_function = false)]
		public DateTime.now_utc ();
		public Gst.DateTime @ref ();
		public GLib.DateTime? to_g_date_time ();
		public string? to_iso8601_string ();
		public void unref ();
		[CCode (has_construct_function = false)]
		public DateTime.y (int year);
		[CCode (has_construct_function = false)]
		public DateTime.ym (int year, int month);
		[CCode (has_construct_function = false)]
		public DateTime.ymd (int year, int month, int day);
	}
}