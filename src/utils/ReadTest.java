package utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ReadTest {

	@Test
	void test() {
//System.out.println(ReadInput.readSCFile());
		assertEquals(ReadInput.readSCFile()[9], 0);
	}

}
