package in.rahul.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import in.rahul.dao.ILoginDao;
import in.rahul.service.LoginServiceImpl;

public class TestLoginServiceImpl {

	private static ILoginDao loginDaoMock;
	
	
	private static LoginServiceImpl loginService;

	@BeforeAll
	public static void setUpOnce() {
		// creating a mock object for DAO class having null method Implementation
		loginDaoMock = Mockito.mock(ILoginDao.class);
		System.out.println("Mock class object is :: " + loginDaoMock.getClass().getName());

		// Create Service class object by using Mock object
		loginService = new LoginServiceImpl(loginDaoMock);
	}

	@Test
	public void testLoginWithcValidCredentials() {
		// Providing stub for DAO authenticate method
		Mockito.when(loginDaoMock.authenticate("root", "pass")).thenReturn(1);

		// calling login method to get the result
		boolean acutalOutput = loginService.login("root", "pass");

		// comparing the boolean result using assert
		assertTrue(acutalOutput);
	}

	@Test
	public void testLoginWithInValidCredentials() {
		// Providing stub for DAO authenticate method
		Mockito.when(loginDaoMock.authenticate("root", "password")).thenReturn(0);

		// calling login method to get the result
		boolean acutalOutput = loginService.login("root", "password");

		// comparing the boolean result using assert
		assertFalse(acutalOutput);
	}

	@Test
	public void testLoginWithNoCredentials() {
		assertThrows(IllegalArgumentException.class, () -> loginService.login("", ""));
	}

	@Test
	public void testRegisterWithSpy() {
		ILoginDao loginDaoSpy = Mockito.spy(ILoginDao.class);
		System.out.println("Spy object is :: " + loginDaoSpy.getClass().getName());
		LoginServiceImpl loginService = new LoginServiceImpl(loginDaoSpy);

		loginService.registerUser("rahul", "admin");
		loginService.registerUser("mohit", "visitor");
		loginService.registerUser("kohli", "");

		Mockito.verify(loginDaoSpy, Mockito.times(1)).addUser("rahul", "admin");
		Mockito.verify(loginDaoSpy, Mockito.times(0)).addUser("mohit", "visitor");
		Mockito.verify(loginDaoSpy, Mockito.never()).addUser("kohli", "");
	}
	
	@AfterAll
	public static void clearOnce() {
		loginDaoMock = null;
		loginService = null;
	}

}
