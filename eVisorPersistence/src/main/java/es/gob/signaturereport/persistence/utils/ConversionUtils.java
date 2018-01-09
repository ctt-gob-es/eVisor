package es.gob.signaturereport.persistence.utils;

import es.gob.signaturereport.configuration.items.Person;
import es.gob.signaturereport.configuration.items.User;
import es.gob.signaturereport.persistence.configuration.model.pojo.PersonaldataPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.UsersPOJO;

public class ConversionUtils {

	/**
	 * Method that includes the information included into {@link Personaldata} object to {@link Person} object.
	 * @param pd	{@link Personaldata} object that contains the information to extract.
	 * @param person	{@link Person} object in which will be included the information.
	 */
	public static void personalData2Person(final PersonaldataPOJO pd, final Person person) {
		person.setName(pd.getPdName());
		person.setSurname(pd.getPdSurname());
		person.setPhone(pd.getPdPhone());
		person.setEmail(pd.getPdEmail());
		person.setPersonId(pd.getPdPk());
		return;
	}
	
	public static void userPOJO2UserData(final UsersPOJO userPOJO, final User user)
	{
		user.setLogin(userPOJO.getUserLogin());
		user.setPassword(userPOJO.getUserPass());
		user.setLocked(userPOJO.getUserLocked());
		PersonaldataPOJO pd = userPOJO.getPersonalData();
		Person person = new Person();
		ConversionUtils.personalData2Person(pd, person);
		user.setPerson(person);

		return;
	}

}
