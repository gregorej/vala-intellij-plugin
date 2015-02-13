namespace SimpleLibrary {
	[CCode (cheader_filename = "lib/mylib.h", type_id = "my_type_in_glib ()")]
    public class MyType {
    	[CCode (has_construct_function = false, type = "MyType*")]
    	public MyType ();
    	[CCode (array_length = false, array_null_terminated = true)]
    	public unowned string[] get_array ();
    }
}