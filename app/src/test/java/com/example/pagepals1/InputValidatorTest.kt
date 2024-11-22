import com.example.pagepals1.utils.InputValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InputValidatorTest {

    @Test
    fun `valid email returns true`() {
        val result = InputValidator.isValidEmail("test@example.com")
        assertTrue(result)
    }

    @Test
    fun `invalid email returns false`() {
        val result = InputValidator.isValidEmail("invalid-email")
        assertFalse(result)
    }

    @Test
    fun `valid password returns true`() {
        val result = InputValidator.isValidPassword("password123")
        assertTrue(result)
    }

    @Test
    fun `short password returns false`() {
        val result = InputValidator.isValidPassword("12345")
        assertFalse(result)
    }

    @Test
    fun `valid name returns true`() {
        val result = InputValidator.isValidName("John")
        assertTrue(result)
    }

    @Test
    fun `blank name returns false`() {
        val result = InputValidator.isValidName("")
        assertFalse(result)
    }
}
