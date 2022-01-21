package eronelit_java_server;

public class server_conf {
    private String Dir_Root, Dir_Default, Dir_404, Dir_Method_Not_Supported;
    private int Port;
    public server_conf(String dir_Root, String dir_Default, String dir_404, String dir_Method_Not_Supported, int port) {
        Dir_Root = dir_Root;
        Dir_Default = dir_Default;
        Dir_404 = dir_404;
        Dir_Method_Not_Supported = dir_Method_Not_Supported;
        Port = port;
    }
    public String getDir_Root() {
        return Dir_Root;
    }
    public void setDir_Root(String dir_Root) {
        Dir_Root = dir_Root;
    }
    public String getDir_Default() {
        return Dir_Default;
    }
    public void setDir_Default(String dir_Default) {
        Dir_Default = dir_Default;
    }
    public String getDir_404() {
        return Dir_404;
    }
    public void setDir_404(String dir_404) {
        Dir_404 = dir_404;
    }
    public String getDir_Method_Not_Supported() {
        return Dir_Method_Not_Supported;
    }
    public void setDir_Method_Not_Supported(String dir_Method_Not_Supported) {
        Dir_Method_Not_Supported = dir_Method_Not_Supported;
    }
    public int getPort() {
        return Port;
    }
    public void setPort(int port) {
        Port = port;
    }


}
