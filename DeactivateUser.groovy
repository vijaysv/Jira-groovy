import com.atlassian.jira.bc.user.UserService
import com.atlassian.jira.component.ComponentAccessor

def userManager = ComponentAccessor.userManager
def userService = ComponentAccessor.getComponent(UserService)

[
    'anuser',
    'otheruser',
    // add more as required
].each { username ->

    def user = userManager.getUserByName(username)
    if (!user) {
        log.warn "Failed to find user with name ${username}"
        return
    }

    def updatedUser = userService.newUserBuilder(user).active(false).build()

    def updateUserValidationResult = userService.validateUpdateUser(updatedUser)

    if (updateUserValidationResult.isValid()) {
        log.warn "Deactivating user ${username}"
        userService.updateUser(updateUserValidationResult)
    } else {
        log.warn "Update of ${user.name} failed: ${updateUserValidationResult.errorCollection.errors.entrySet().join(',')}\n"
    }
}
