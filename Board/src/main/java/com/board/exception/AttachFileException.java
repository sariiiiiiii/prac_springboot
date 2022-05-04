package com.board.exception;

@SuppressWarnings("serial")
public class AttachFileException extends RuntimeException{
	
	
	/** uploadFiles 메소드의 file.transferTo 메소드는 서버에 물리적으로 파일을 생성하는 기능이고, 파일 생성은 디스크에 영향을 주는 I/O 작업이다 */
	/** transferTo 메소드는 IOException과 IllegalStateException를 호출한 쪽에서 처리하게(Throw) 되어 있는데 */
	/** IOException은 반드시 예외 처리를 해야 하는 try ~ catch 작업을 해줘야 한다 */
	/** AttachFileException 클래스는 RuntimeException을 상속하고 IOException을 처리하는 catch문에서 Unchecked Exception을 throw하기 때문에 */
	/** transferTo 메소드를 사용하는 곳에서는 아무런 예외처리를 진행하지 않아도 된다 */
	
	
	public AttachFileException(String message) {
		super(message);
	}
	
	public AttachFileException(String message, Throwable cause) {
		super(message, cause);
	}

}	
