public interface Callable : GLib.Object {
   public abstract bool answering { get; protected set; }
   public abstract void answer ();
   public abstract bool hang ();
   public static bool default_hang (Callable call)
   {
      stdout.printf ("At Callable.hang()\n");
      call.answering = false;
      return true;
   }
}

public abstract class Caller : GLib.Object, Callable
{
   public bool answering { get; protected set; }
   public void answer ()
   {
     stdout.printf ("At Caller.answer()\n");
     answering = true;
     hang ();
   }
   public virtual bool hang () { return Callable.default_hang (this); }
}

public class TechPhone : Caller {
        public string number { get; set; }
}

public class Phone : Caller {
   public override bool hang () {
        stdout.printf ("At Phone.hang()\n");
        return false;
   }
   
   public static void main ()
   {
      var f = (Callable) new Phone ();
      f.answer ();
      if (f.hang ())
         stdout.printf("Hand done.\n");
      else
         stdout.printf("Hand Error!\n");
      
      var t = (Callable) new TechPhone ();
      t.answer ();
      if (t.hang ())
         stdout.printf("Tech Hand done.\n");
      else
         stdout.printf("Tech Hand Error!\n");
      stdout.printf("END\n");
   }
}