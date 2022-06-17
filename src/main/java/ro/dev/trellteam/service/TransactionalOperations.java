package ro.dev.trellteam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.dev.trellteam.model.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionalOperations {

    private final OrganisationService organisationService;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
    private final AccountService accountService;
    private final RoleService roleService;
    private final CardService cardService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final CardLogService cardLogService;

    @Transactional
    public void createOrganisationRepository(Organisation organisation, Department department, Employee employee, Account account) {
        log.debug("TransactionalOperations--createOrganisationRepository--IN");
        //employee = employeeService.saveAndFlush(employee);

        final Role role = roleService.findByName("ADMIN");
        account.addRole(role);
        account.setEmployee(employee);
        account = accountService.save(account, true);

        department.addEmployee(employee);
        organisation.addEmployee(employee);

        organisation.addDepartment(department);
        organisation = organisationService.save(organisation);

        log.debug("TransactionalOperations--createOrganisationRepository--organisation: {}", organisation.toString());
        log.debug("TransactionalOperations--createOrganisationRepository--department: {}", department);
        log.debug("TransactionalOperations--createOrganisationRepository--employee: {}", employee.toString());
        log.debug("TransactionalOperations--createOrganisationRepository--account: {}", account.toString());

        log.debug("TransactionalOperations--createOrganisationRepository--OUT");
    }

    @Transactional
    public void createEmployee(Account account, Employee employee, Department department) {
        log.debug("TransactionalOperations--createEmployee--IN");

        employee = employeeService.save(employee);

        account.setEmployee(employee);
        department.addEmployee(employee);

        Organisation organisation = organisationService.findByDepartmentId(department.getId());
        organisation.addEmployee(employee);

        account = accountService.save(account, true);
        department = departmentService.save(department);
        organisation = organisationService.save(organisation);

        log.debug("TransactionalOperations--createEmployee--organisation: {}", organisation.toString());
        log.debug("TransactionalOperations--createEmployee--employee: {}", employee.toString());
        log.debug("TransactionalOperations--createEmployee--account: {}", account.toString());
        log.debug("TransactionalOperations--createEmployee--department: {}", department.toString());
        log.debug("TransactionalOperations--createEmployee--OUT");
    }

    @Transactional
    public Department createDepartment(Organisation organisation, Department department) {
        log.debug("TransactionalOperations--removeDepartment--IN");
        log.debug("TransactionalOperations--removeDepartment--organisation: {}", organisation.toString());
        log.debug("TransactionalOperations--removeDepartment--department: {}", department.toString());

        boolean isRemoved = false;
        try {
            department = departmentService.save(department);
            organisation.addDepartment(department);
            organisationService.save(organisation);
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
            departmentService.deleteById(department.getId());
            organisationService.save(organisation);
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
            departmentService.save(department);
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
            departmentService.save(department);
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

        cardLog = cardLogService.save(cardLog);
        card.addLog(cardLog);
        log.debug("TransactionalOperations--createCard--cardLog: {}", cardLog);

        card = cardService.save(card);
        log.debug("TransactionalOperations--createCard--card: {}", card);

        board.addCard(card);
        board = boardService.createBoard(board);
        log.debug("TransactionalOperations--createCard--board: {}", board);
        log.debug("TransactionalOperations--createCard--OUT");

        return card;
    }

    @Transactional
    public Card createCardComment(Card card, Comment comment, CardLog cardLog) {
        log.debug("TransactionalOperations--createCardComment--IN");

        comment = commentService.save(comment);
        cardLog = cardLogService.save(cardLog);

        card.addComment(comment);
        card.addLog(cardLog);

        log.debug("TransactionalOperations--createCardComment--comment: {}", comment);
        log.debug("TransactionalOperations--createCardComment--cardLog: {}", cardLog);

        card = cardService.save(card);

        log.debug("TransactionalOperations--createCardComment--card: {}", card);
        log.debug("TransactionalOperations--createCardComment--OUT");

        return card;
    }

    @Transactional
    public Card changeCardStatus(Card card, CardLog cardLog, String status) {
        log.debug("TransactionalOperations--changeCardStatus--IN");

        cardLog = cardLogService.save(cardLog);

        card.setStatus(status);
        card.addLog(cardLog);

        log.debug("TransactionalOperations--changeCardStatus--status: {}", status);
        log.debug("TransactionalOperations--changeCardStatus--cardLog: {}", cardLog);

        card = cardService.save(card);

        log.debug("TransactionalOperations--changeCardStatus--card: {}", card);
        log.debug("TransactionalOperations--changeCardStatus--OUT");

        return card;
    }

    @Transactional
    public Card updateCard(Card card, CardLog cardLog) {
        log.debug("TransactionalOperations--updateCard--IN");

        cardLog = cardLogService.save(cardLog);
        card.addLog(cardLog);
        card = cardService.save(card);

        log.debug("TransactionalOperations--updateCard--cardLog: {}", cardLog);
        log.debug("TransactionalOperations--updateCard--card: {}", card);
        log.debug("TransactionalOperations--updateCard--OUT");
        return card;
    }

    @Transactional
    public Card assignCard(Card card, CardLog cardLog) {
        log.debug("TransactionalOperations--assignCard--IN");

        cardLog = cardLogService.save(cardLog);
        card.addLog(cardLog);
        card = cardService.save(card);

        log.debug("TransactionalOperations--assignCard--cardLog: {}", cardLog);
        log.debug("TransactionalOperations--assignCard--card: {}", card);
        log.debug("TransactionalOperations--assignCard--OUT");
        return card;
    }

    @Transactional
    public Card unassignCard(Card card, CardLog cardLog) {
        log.debug("TransactionalOperations--unassignCard--IN");

        cardLog = cardLogService.save(cardLog);
        card.addLog(cardLog);
        card = cardService.save(card);

        log.debug("TransactionalOperations--unassignCard--cardLog: {}", cardLog);
        log.debug("TransactionalOperations--unassignCard--card: {}", card);
        log.debug("TransactionalOperations--unassignCard--OUT");
        return card;
    }
}
