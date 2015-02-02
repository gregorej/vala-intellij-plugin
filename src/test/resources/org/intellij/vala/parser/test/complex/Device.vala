namespace MTP {
	public class Device : GLib.Object {
		internal LibMTP.Device* handle;
		
		~Device() {
			handle->release();
		}
		
		internal Device (LibMTP.RawDevice rd, LibMTP.Device* handle) {
			this.handle = handle;
			bus_location = rd.bus_location;
			devnum = rd.devnum;
		}
		
		public uint bus_location { get; private set; }
		public uint devnum { get; private set; }
		
		public string device_version {
			owned get {
				return handle->device_version;
			}
		}
		
		public string friendly_name {
			owned get {
				return handle->friendly_name;
			}
		}
		
		public string manufacturer_name {
			owned get {
				return handle->manufacturer_name;
			}
		}
		
		public string model_name {
			owned get {
				return handle->model_name;
			}
		}
		
		public string serial_number {
			owned get {
				return handle->serial_number;
			}
		}
		
		public DeviceStorage[] storages {
			owned get {
				var list = new GenericArray<DeviceStorage>();
				LibMTP.DeviceStorage* s = handle->storage;
				while (s != null) {
					list.add (new DeviceStorage (this, s));
					s = s->next;
				}
				return list.data;
			}
		}
	}
}
