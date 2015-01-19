int main (string[] args) <fold text='{...}' expand='true'>{

    //statements folding
    while ((info = enumerator.next_file (null)) != null) <fold text='{...}' expand='true'>{
        if (info.get_name ().has_prefix ("video")){
            debug ("camera found: %s", info.get_name ());
            return true;
        }
    }</fold>

    //lambda expression body folding
    this.camera.capture_stop.connect (() => <fold text='{...}' expand='true'>{
        // Enable extra buttons
        gallery_button.sensitive = gallery_files_exists ();
        this.mode_button.sensitive = true;
        this.set_take_button_icon (this.camera.get_action_type ());
    }</fold>);

    return 0;
}</fold>