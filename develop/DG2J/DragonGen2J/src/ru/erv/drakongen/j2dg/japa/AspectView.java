package ru.erv.drakongen.j2dg.japa;

class AspectView {
	protected String color1 ="#E5FFFF";
	protected String color2 ="#E5FFFF";
	protected String name = "";
	//public int line_width = 1;
	
	
	public AspectView() {
	}
	
	public AspectView(String name, String color1, String color2/*, int line_width*/) {
		this.color1 = color1;
		this.color2 = color2;
		this.name = name;
		//this.line_width = line_width;
	}

	public String getColor1() {
		return color1;
	}

	public String getColor2() {
		return color2;
	}
	
	public String toString() {
		return name;
	}


}
