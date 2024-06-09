package com.example.logviewe.param;

public enum PlayId {
	DRAW(0,"ドロー"),
	PLAY(1,"使用した"),
	DISCARD(2,"捨てた"),
	ATTACH(3,"つける"),
	PRIZE(4,"サイドをとる"),
	KO(4,"気絶"),
	EVOLE(5,"進化"),
	POSONED(6,"毒"),
	BENCH(7,"ベンチへ出す"),
	ACTIVE(8,"バトル場へ出す"),
	USE(9,"特性を使う"),
	STUDIUM(10,"スタジアムを出す"),
	ELSE(99,"その他");
	

	//ステータス
	private int id;
	private String msg;

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return msg1
	 */
	public String getMsg() {
		return msg;
	}


	private PlayId(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}

}
