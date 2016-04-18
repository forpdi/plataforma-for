package org.forpdi.core.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.forpdi.core.company.CompanyBS;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.boilerplate.util.CryptManager;

/**
 * @author Renato R. R. de Oliveira
 */
@RequestScoped
public class UserBS extends HibernateBusiness {
	
	@Inject private CompanyBS companyBS;
	
	public void save(User user) {
		if (this.existsByEmail(user.getEmail()) != null) {
			throw new IllegalArgumentException("Já existe um usuários cadastrado com este e-mail.");
		}
		user.setPassword(CryptManager.passwordHash(user.getPassword()));
		user.setActive(true);
		user.setDeleted(false);
		user.setCreation(new Date());
		this.persist(user);
	}
	
	public User existsByEmailAndPassword(String email, String pwd) {
		Criteria criteria =
			this.dao.newCriteria(User.class)
			.add(Restrictions.eq("deleted", false))
			.add(Restrictions.eq("email", email))
			.add(Restrictions.eq("password", CryptManager.passwordHash(pwd)))
		;
		return (User) criteria.uniqueResult();
	}
	
	public User existsByEmail(String email) {
		Criteria criteria =
			this.dao.newCriteria(User.class)
			.add(Restrictions.eq("email", email))
		;
		return (User) criteria.uniqueResult();
	}
	
	public PaginatedList<User> list(int page) {
		PaginatedList<User> results = new PaginatedList<User>();
		Criteria criteria =
			this.dao.newCriteria(User.class)
			.setFirstResult(page*10).setMaxResults(10)
			.addOrder(Order.asc("name"))
		;
		Criteria counting =
			this.dao.newCriteria(User.class)
			.setProjection(Projections.countDistinct("id"))
		;
		results.setList(this.dao.findByCriteria(criteria, User.class));
		results.setTotal((Long) counting.uniqueResult());
		return results;
	}
	
	public int updateUsersActivationState() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		LocalDate today = LocalDate.now();
		StringBuilder sql = new StringBuilder();
		sql
			.append("UPDATE ")
			.append(User.TABLE)
			.append(" SET active=0 WHERE accessLevel <= ")
			.append(UserRole.NORMAL.getAccessLevel())
			.append(" AND activeUntil < '")
			.append(today.format(formatter))
			.append("'")
		;
		return this.dao.bulkUpdate(sql.toString());
	}
}
