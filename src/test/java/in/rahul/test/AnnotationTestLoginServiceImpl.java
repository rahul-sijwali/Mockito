package in.rahul.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import in.rahul.dao.ILoginDao;
import in.rahul.service.LoginServiceImpl;

public class AnnotationTestLoginServiceImpl {

	@Mock
	private static ILoginDao loginDaoMock;
	
	@Spy
	private static ILoginDao loginDaoSpy;

	@InjectMocks
	private static LoginServiceImpl loginService;
	
	
	public AnnotationTestLoginServiceImpl() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testLoginWithValidCredentials() {
		System.out.println(loginService);

		// Providing stub for DAO authenticate method
		Mockito.when(loginDaoMock.authenticate("root", "pass")).thenReturn(1);

		// calling login method to get the result
		boolean acutalOutput = loginService.login("root", "pass");

		// comparing the boolean result using assert
		assertTrue(acutalOutput);
	}

	@Test
	public void testLoginWithInValidCredentials() {
		System.out.println(loginService);
		
		// Provide stub for DAO authenticate method
		Mockito.when(loginDaoMock.authenticate("root", "password")).thenReturn(0);

		// calling login method to get the result
		boolean acutalOutput = loginService.login("root", "password");

		// comparing the boolean result using assert
		assertFalse(acutalOutput);
	}

	@Test
	public void testLoginWithNoCredentials() {
		System.out.println(loginService);
		assertThrows(IllegalArgumentException.class, () -> loginService.login("", ""));
	}
	
	@Test
	public void testRegisterWithSpy() {
		ILoginDao loginDaoSpy = Mockito.spy(ILoginDao.class);
		System.out.println("Spy object is :: " + loginDaoSpy.getClass().getName());
		LoginServiceImpl loginService = new LoginServiceImpl(loginDaoSpy);
		System.out.println("AnnotationTestLoginServiceImpl.testRegisterWithSpy() :: "+loginService);
		
		loginService.registerUser("rahul", "admin");
		loginService.registerUser("mohit", "visitor");
		loginService.registerUser("kohli", "");

		Mockito.verify(loginDaoSpy, Mockito.times(1)).addUser("rahul", "admin");
		Mockito.verify(loginDaoSpy, Mockito.times(0)).addUser("mohit", "visitor");
		Mockito.verify(loginDaoSpy, Mockito.never()).addUser("kohli", "");
	}

}
