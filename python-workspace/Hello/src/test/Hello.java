package test;

public class Hello {
	
	private native void sayHello();
	
	static {
		System.loadLibrary("hello");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Hello hello = new Hello();
		hello.sayHello();
		System.out.println(args.length);
	}

}
