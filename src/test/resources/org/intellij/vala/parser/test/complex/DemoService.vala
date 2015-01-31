[DBus(name = "org.example.DemoService")]
public class DemoService : Object {
    /* Private field, not exported via D-Bus */
    int counter;

    /* Public field, not exported via D-Bus */
    public int status;

    /* Public property, exported via D-Bus */
    public int something { get; set; }

    /* Public signal, exported via D-Bus
     * Can be emitted on the server side and can be connected to on the client side.
     */
    public signal void sig1();

    /* Public method, exported via D-Bus */
    public void some_method() {
        counter++;
        stdout.printf("heureka! counter = %d\n", counter);
        sig1();  // emit signal
    }

    /* Public method, exported via D-Bus and showing the sender who is
       is calling the method (not exported in the D-Bus interface) */
    public void some_method_sender(string message, GLib.BusName sender) {
        counter++;
        stdout.printf("heureka! counter = %d, '%s' message from sender %s\n",
                      counter, message, sender);
    }
}