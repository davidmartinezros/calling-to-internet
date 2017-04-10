package org.dmr;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

import org.dmr.domain.impl.CallUrlImpl;

/**
 * Created by davidmartinezros on 10/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CallingToInternetApplicationTests {

	private CallUrlImpl lru;

	@Before
	public void setUp(){
		lru = CallUrlImpl.getInstance();
	}

	@Test
	public void lru_add_values_with_first_item_removed(){
		lru.put(1, "aaa");
		lru.put(2, "bbb");
		lru.put(3, "ccc");
		lru.put(4, "ddd");

		assertEquals(lru.toString(), "bbb ccc ddd ");
	}

	@Test
	public void lru_add_values_and_use_first() throws Exception {
		lru.put(1, "aaa");
		lru.put(2, "bbb");
		lru.put(3, "ccc");
		lru.get(1);
		lru.put(4, "ddd");

		assertEquals(lru.toString(), "ccc aaa ddd ");
	}

	@Test
	public void lru_add_values_and_use_second() throws Exception {
		lru.put(1, "aaa");
		lru.put(2, "bbb");
		lru.put(3, "ccc");
		lru.get(2);
		lru.put(4, "ddd");

		assertEquals(lru.toString(), "ccc bbb ddd ");
	}

	@Test(expected = NullPointerException.class)
	public void lru_add_values_and_get_first() throws Exception {
		lru.put(1, "aaa");
		lru.put(2, "bbb");
		lru.put(3, "ccc");
		lru.put(4, "ddd");
		lru.get(1);
	}

}
