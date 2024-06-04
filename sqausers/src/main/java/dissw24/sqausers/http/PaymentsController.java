package dissw24.sqausers.http;

import java.util.Map;

import javax.servlet.http.HttpSession;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.json.*;

import dissw24.sqausers.model.CredencialesRegistro;
import dissw24.sqausers.model.User;
import dissw24.sqausers.services.UserService;

//latest version
@RestController
@RequestMapping("payments")
@CrossOrigin("*")
public class PaymentsController {
	
	static {
		Stripe.apiKey = "sk_test_51P1sGXHww0tUpksiVszaaVCX6Upk9Knwt7SUPYAYlFM8gLDouAVDDXMbMyq2AOv7LSytnacos5S5teHWOZ3ulZUz00KcOQjzN8";
	}
	
	@GetMapping("/prepagar")
	public Map<String, String> prepagar(@RequestParam double importe) {
		long total = (long) (importe*100);
		
		PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
				.setCurrency("eur")
				.setAmount(total)
				.build();
		try {
			PaymentIntent intent = PaymentIntent.create(params);
			JSONObject jso = new JSONObject(intent.toJson());
			String clientSecret = jso.getString("client_secret");
			Map<String, String> resultado = new HashMap<>();
			resultado.put("client_secret", clientSecret);
			
			return resultado;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null; //no está bien?
	}
	
}
