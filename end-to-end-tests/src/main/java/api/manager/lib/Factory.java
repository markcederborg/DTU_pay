package api.manager.lib;

import api.RestClient;
import api.manager.impl.ManagerApp;

public class Factory {
	static ManagerApp app = null;

	public synchronized ManagerApp getApp() {
		if (app != null) {
			return app;
		}

		RestClient api = new RestClient("http://localhost:8084/manager");
		app = new ManagerApp(api);

		return app;
	}
}
