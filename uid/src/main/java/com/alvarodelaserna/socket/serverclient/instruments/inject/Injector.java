package com.alvarodelaserna.socket.serverclient.instruments.inject;

public interface Injector {

	<T> T inject(Class<T> tClass);

}
