namespace SimpleLibrary {
	[CCode (cheader_filename = "lib/mylib.h", type_id = "my_type_in_glib ()")]
    public class MyType {
    	[CCode (has_construct_function = false, type = "MyType*")]
    	public MyType ();
    	[CCode (array_length = false, array_null_terminated = true)]
    	public unowned string[] get_array ();
    	public Action (string name, string? label, string? tooltip, string? stock_id);
    	public string name { get; construct; }
    	public GLib.List<weak Gtk.Action> list_actions ();
    }
}