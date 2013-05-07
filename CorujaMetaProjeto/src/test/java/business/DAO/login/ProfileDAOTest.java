package business.DAO.login;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import persistence.dto.Profile;
import business.exceptions.login.ProfileNotFoundException;
import business.exceptions.login.UnreachableDataBaseException;

public class ProfileDAOTest {

	@Test
	@Before
	public synchronized void testCreateProfileStringStringArray() throws UnreachableDataBaseException {
		ProfileDAO dao = new ProfileDAO();
		dao.createProfile("teste", new String[]{"default,user,admin,master,legend"});
	}

	@Test
	@After
	public synchronized void testFindProfileByName() throws ProfileNotFoundException, UnreachableDataBaseException {
		ProfileDAO dao = new ProfileDAO();
		Profile l = dao.findProfileByName("teste");
		dao.removeProfile("teste");
		for(String s : l.getPermissions()){
			System.out.println(s);
		}
		assertTrue(l.getProfile().equals("teste"));
	}

}
