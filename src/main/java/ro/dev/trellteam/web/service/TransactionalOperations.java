package ro.dev.trellteam.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.dev.trellteam.domain.Account;
import ro.dev.trellteam.domain.Board;
import ro.dev.trellteam.domain.Card;
import ro.dev.trellteam.domain.CardLog;
import ro.dev.trellteam.domain.Comment;
import ro.dev.trellteam.domain.Department;
import ro.dev.trellteam.domain.Employee;
import ro.dev.trellteam.domain.Organisation;
import ro.dev.trellteam.domain.Role;
import ro.dev.trellteam.enums.CardStatusEnum;
import ro.dev.trellteam.web.repository.AccountRepository;
import ro.dev.trellteam.web.repository.BoardRepository;
import ro.dev.trellteam.web.repository.CardLogRepository;
import ro.dev.trellteam.web.repository.CardRepository;
import ro.dev.trellteam.web.repository.CommentRepository;
import ro.dev.trellteam.web.repository.DepartmentRepository;
import ro.dev.trellteam.web.repository.EmployeeRepository;
import ro.dev.trellteam.web.repository.OrganisationRepository;
import ro.dev.trellteam.web.repository.RoleRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionalOperations {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final OrganisationRepository organisationRepository;
    private final DepartmentRepository departmentRepository;
    private final BoardRepository boardRepository;
    private final CardRepository cardRepository;
    private final CardLogRepository cardLogRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createOrganisationRepository(Organisation organisation, Department department, Employee employee, Account account) {
        log.debug("TransactionalOperations--createOrganisationRepository--IN");
        employee = employeeRepository.saveAndFlush(employee);

        final Role role = roleRepository.findByName("ADMIN");
        account.addRole(role);
        account.setEmployee(employee);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account = accountRepository.save(account);

        department.addEmployee(employee);
        organisation.addEmployee(employee);

        organisation.addDepartment(department);
        organisation = organisationRepository.save(organisation);

        log.debug("TransactionalOperations--createOrganisationRepository--organisation: {}", organisation.toString());
        log.debug("TransactionalOperations--createOrganisationRepository--department: {}", department);
        log.debug("TransactionalOperations--createOrganisationRepository--employee: {}", employee.toString());
        log.debug("TransactionalOperations--createOrganisationRepository--account: {}", account.toString());

        log.debug("TransactionalOperations--createOrganisationRepository--OUT");
    }

    @Transactional
    public Account createEmployee(Account account, Employee employee, Department department) {
        log.debug("TransactionalOperations--createEmployee--IN");

        employee = employeeRepository.save(employee);

        account.setEmployee(employee);
        department.addEmployee(employee);

        Organisation organisation = organisationRepository.findByDepartmentId(department.getId());
        organisation.addEmployee(employee);

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account = accountRepository.save(account);
        department = departmentRepository.save(department);
        organisation = organisationRepository.save(organisation);

        log.debug("TransactionalOperations--createEmployee--organisation: {}", organisation.toString());
        log.debug("TransactionalOperations--createEmployee--employee: {}", employee.toString());
        log.debug("TransactionalOperations--createEmployee--account: {}", account.toString());
        log.debug("TransactionalOperations--createEmployee--department: {}", department.toString());
        return account;
    }

    @Transactional
    public Department createDepartment(Organisation organisation, Department department) {
        log.debug("TransactionalOperations--removeDepartment--IN");
        log.debug("TransactionalOperations--removeDepartment--organisation: {}", organisation.toString());
        log.debug("TransactionalOperations--removeDepartment--department: {}", department.toString());

        boolean isRemoved = false;
        try {
            department = departmentRepository.save(department);
            organisation.addDepartment(department);
            organisationRepository.save(organisation);
            isRemoved = true;
        } catch(final Exception e) {
            log.error(e.getMessage());
        }

        log.debug("TransactionalOperations--removeDepartment--isRemoved: {}", isRemoved);
        log.debug("TransactionalOperations--removeDepartment--OUT");
        return isRemoved == true ? department : null;
    }

    @Transactional
    public boolean removeDepartment(Organisation organisation, Department department) {
        log.debug("TransactionalOperations--removeDepartment--IN");
        log.debug("TransactionalOperations--removeDepartment--organisation: {}", organisation.toString());
        log.debug("TransactionalOperations--removeDepartment--department: {}", department.toString());

        boolean isRemoved = false;
        try {
            organisation.removeDepartment(department);
            department.purgeEmployees();
            departmentRepository.deleteById(department.getId());
            organisationRepository.save(organisation);
            isRemoved = true;
        } catch(final Exception e) {
            log.error(e.getMessage());
        }

        log.debug("TransactionalOperations--removeDepartment--isRemoved: {}", isRemoved);
        log.debug("TransactionalOperations--removeDepartment--OUT");
        return isRemoved;
    }

    @Transactional
    public Department assignEmployeeToDepartment(Employee employee, Department department) {
        log.debug("TransactionalOperations--assignEmployeeToDepartment--IN");
        log.debug("TransactionalOperations--assignEmployeeToDepartment--employee: {}", employee.toString());
        log.debug("TransactionalOperations--assignEmployeeToDepartment--department: {}", department.toString());

        boolean isAssigned = false;
        try {
            department.addEmployee(employee);
            departmentRepository.save(department);
            isAssigned = true;
        } catch(final Exception e) {
            log.error(e.getMessage());
        }

        log.debug("TransactionalOperations--assignEmployeeToDepartment--isAssigned: {}", isAssigned);
        log.debug("TransactionalOperations--assignEmployeeToDepartment--OUT");

        return isAssigned == true ? department : null;
    }

    @Transactional
    public Department unassignEmployeeFromDepartment(Employee employee, Department department) {
        log.debug("TransactionalOperations--unassignEmployeeFromDepartment--IN");
        log.debug("TransactionalOperations--unassignEmployeeFromDepartment--employee: {}", employee.toString());
        log.debug("TransactionalOperations--unassignEmployeeFromDepartment--department: {}", department.toString());

        boolean isUnassigned = false;
        try {
            department.removeEmployee(employee);
            departmentRepository.save(department);
            isUnassigned = true;
        } catch(final Exception e) {
            log.error(e.getMessage());
        }

        log.debug("TransactionalOperations--unassignEmployeeFromDepartment--isUnassigned: {}", isUnassigned);
        log.debug("TransactionalOperations--unassignEmployeeFromDepartment--OUT");

        return isUnassigned == true ? department : null;
    }

    @Transactional
    public Card createCard(Board board, Card card, CardLog cardLog) {
        log.debug("TransactionalOperations--createCard--IN");

        cardLog = cardLogRepository.save(cardLog);
        card.addLog(cardLog);
        log.debug("TransactionalOperations--createCard--cardLog: {}", cardLog);

        card = cardRepository.save(card);
        log.debug("TransactionalOperations--createCard--card: {}", card);

        board.addCard(card);
        board = boardRepository.save(board);
        log.debug("TransactionalOperations--createCard--board: {}", board);
        log.debug("TransactionalOperations--createCard--OUT");

        return card;
    }

    @Transactional
    public Card createCardComment(Card card, Comment comment, CardLog cardLog) {
        log.debug("TransactionalOperations--createCardComment--IN");

        comment = commentRepository.save(comment);
        cardLog = cardLogRepository.save(cardLog);

        card.addComment(comment);
        card.addLog(cardLog);

        log.debug("TransactionalOperations--createCardComment--comment: {}", comment);
        log.debug("TransactionalOperations--createCardComment--cardLog: {}", cardLog);

        card = cardRepository.save(card);

        log.debug("TransactionalOperations--createCardComment--card: {}", card);
        log.debug("TransactionalOperations--createCardComment--OUT");

        return card;
    }

    @Transactional
    public Card changeCardStatus(Card card, CardLog cardLog, CardStatusEnum status) {
        log.debug("TransactionalOperations--changeCardStatus--IN");

        cardLog = cardLogRepository.save(cardLog);

        card.setStatus(status);
        card.addLog(cardLog);

        log.debug("TransactionalOperations--changeCardStatus--status: {}", status);
        log.debug("TransactionalOperations--changeCardStatus--cardLog: {}", cardLog);

        card = cardRepository.save(card);

        log.debug("TransactionalOperations--changeCardStatus--card: {}", card);
        log.debug("TransactionalOperations--changeCardStatus--OUT");

        return card;
    }

    @Transactional
    public Card updateCard(Card card, CardLog cardLog) {
        log.debug("TransactionalOperations--updateCard--IN");

        cardLog = cardLogRepository.save(cardLog);
        card.addLog(cardLog);
        card = cardRepository.save(card);

        log.debug("TransactionalOperations--updateCard--cardLog: {}", cardLog);
        log.debug("TransactionalOperations--updateCard--card: {}", card);
        log.debug("TransactionalOperations--updateCard--OUT");
        return card;
    }

    @Transactional
    public Card assignCard(Card card, CardLog cardLog) {
        log.debug("TransactionalOperations--assignCard--IN");

        cardLog = cardLogRepository.save(cardLog);
        card.addLog(cardLog);
        card = cardRepository.save(card);

        log.debug("TransactionalOperations--assignCard--cardLog: {}", cardLog);
        log.debug("TransactionalOperations--assignCard--card: {}", card);
        log.debug("TransactionalOperations--assignCard--OUT");
        return card;
    }

    @Transactional
    public Card unassignCard(Card card, CardLog cardLog) {
        log.debug("TransactionalOperations--unassignCard--IN");

        cardLog = cardLogRepository.save(cardLog);
        card.addLog(cardLog);
        card = cardRepository.save(card);

        log.debug("TransactionalOperations--unassignCard--cardLog: {}", cardLog);
        log.debug("TransactionalOperations--unassignCard--card: {}", card);
        log.debug("TransactionalOperations--unassignCard--OUT");
        return card;
    }
}
