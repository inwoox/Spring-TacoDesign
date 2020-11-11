package sia5;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.transformer.GenericTransformer;

@Configuration
public class FileWriterIntegrationConfig {
	
	// 자바 구성을 사용한 통합 플로우 정의  -----------------
	
	// 변환기
	@Profile("javaconfig")
	@Bean
	// input 채널의 메시지를 받아서 output 채널에 쓰는 통합 플로우 변환기
	@Transformer(inputChannel="textInChannel", outputChannel="fileWriterChannel")
	public GenericTransformer<String, String> upperCaseTransformer(){
		return text -> text.toUpperCase();
	}
	
	// 파일 쓰기 메시지 핸들러
	@Profile("javaconfig")
	@Bean
	// input 채널의 메시지를 받아서, 메시지 핸들러(서비스)에 넘겨준다.
	@ServiceActivator(inputChannel="fileWriterChannel")
	
	//메시지 핸들러는 , 메시지 페이로드를 지정된 디렉터리의 파일에 쓴다. 이때 파일 이름은, 해당 메시지의 file_name 헤더에 지정된 것을 사용한다.
	public FileWritingMessageHandler fileWriter() {
		FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("/tmp/sia5/files"));  // 절대 경로
		handler.setExpectReply(false); // 이 메서드는 서비스에서 응답 채널(플로우의 업스트림 컴포넌트로 값이 반환될 수 있는 채널)을 사용하지 않음을 나타낸다.
		handler.setFileExistsMode(FileExistsMode.APPEND);
		handler.setAppendNewLine(true);
		return handler;
	}
	
	
	
	
	// DSL (Domain Specific Language) 을 사용한 자바 구성을 사용한 통합 플로우 정의  -------------
	
	// IntegrationFlow 클래스는 플로우를 선언할 수 있는 빌더 API를 시작시킨다.
	// 인바운드 채널에서 메시지를 수신하면서 시작되어, 메시지 페이로드를 대문자로 바꾸는 변환기가 실행되고
	// 스프링 통합 파일 모듈에 제공되는 Files 타입으로부터 생성된 아웃바운드 채널 어댑터에서 처리된 후
	// get을 통해 return 문에서 반환되는 IntegrationFlow 인스턴스를 가져온다.
	@Profile("javadsl")
	@Bean
	public IntegrationFlow fileWriterFlow() { 
		return IntegrationFlows
				.from(MessageChannels.direct("textInChannel"))     // 인바운드 채널
				.<String, String> transform(t -> t.toUpperCase())  // 변환기
				//.transform(RomanNumbers::toRoman)                // 메서드 참조를 통한 변환기
				//.channel(MessageChannels.direct("orderChannel"))
				.handle(Files
						.outboundAdapter(new File("/tmp/sia5/files"))
						.fileExistsMode(FileExistsMode.APPEND)
						.appendNewLine(true))
				.get();
	}
	
	
//  !! 메시지 채널
	
//  메시지 채널은 통합 파이프라인을 통해, 메시지가 이동하는 수단
//  채널들을 별도로 선언하지 않는다. 각각 채널 이름의 빈이 없으면, 이 채널들은 자동으로 생성된다.
//  하지만 각 채널의 구성 방법을 더 제어하고 싶을 경우에는, 별도의 빈으로 구성 (입력 채널 같은 경우, 기본적으로 DirectChannel이 사용되지만 변경할 수 있다)
//  @Bean
//  public MessageChannel orderChannel(){ return new PublishSubscribeChannel(); }
//  그리고 예를 들어 @ServiceActivator(inputChannel="orderChannel") 처럼 참조한다.
	

// 	!! 필터
	
//	필터는 통합 파이프라인의 중간에 위치할 수 있으며, 플로우의 전 단계로부터 다음 단계로의 메시지 전달을 허용 또는 불허한다.
//	자바 구성
//	@Filter(inputChannel="xx", outputChannel="xxx")
//	public boolean evenNumberFilter(Integer number) { return number % 2 == 0; }

//	DSL 사용 구성
//	@Bean
//	public IntegrationFlow evenNumFlow(AtomicInteger integerSource) {
// 		return IntegrationFlows
//				...
// 				.<Integer>filter((p) -> p % 2 == 0)
//				...
//				.get();
//	}
	
	
// 	!! 변환기
	
// 	input 채널에서 Integer 값을 수신하고, static 메서드인 toRoman을 사용해 변환을 수행하고, 변환 결과를 output 채널로 전송한다.
// 	숫자를 로마숫자를 포함하는 문자열로 변환하는 변환기
// 	@Bean
// 	@Transformer(inputChannel="numberChannel", outputChannel="romanNumberChannel")
// 	public GenericTransformer<Integer, String> romanNumTransformer() { return RomanNumbers::toRoman; }
	
	
// 	!! 라우터
	
//	전달 조건을 기반으로 통합 플로우 내부를 분기 (조건에 따라 홀수 채널로 보내거나, 짝수 채널로 보낸다)
//	@Bean
//	@Router(inputChannel="numberChannel")
//	public AbstractMessageRouter evenOddRouter() {
//		return new AbstractMessageRouter() {
//			@Override
//			protected Collection<MessageChannel> determineTargetChannels(Message<?> message){
//				Integer number = (Integer) message.getPayload();
//				if (number % 2 == 0) { return Collections.singleton(evenChannel()); }  // 짝수 메시지는 evenChannel로
//				return Collections.singleton(oddChannel()); 													 // 홀수 메시지는 oddChannel로 전달
//			}													
//		};
//	}
//	
//	@Bean
//	public MessageChannel evenChannel() { return new DirectChannel(); }
//	
//	@Bean
//	public MessageChannel oddChannel() { return new DirectChannel(); }

//	DSL 사용 구성
//	@Bean
//	public IntegrationFlow evenNumFlow(AtomicInteger integerSource) {
//		return IntegrationFlows
//			...
//				.<Integer, String> route(n -> n%2 == 0 ? "EVEN" : "ODD" , mapping -> mapping
//				.subFlowMapping("EVEN", sf -> sf.<Integer, Integer> transform(n -> n * 10).handle((i,h) -> { ... }))
//				.subFlowMapping("ODD", sf -> sf.transform(RomanNumbers::toRoman).handle((i,h) -> ({ ... })))
//				.get();
//}

	
//	!! 분배기
	
//	통합 플로우에서 하나의 메시지를 여러개로 분할해, 독립적으로 처리하는 것이 유용할 수 있다.
//	두가지 경우에 유용할 수 있다. 
//	1. 같은 제품 타입의 컬렉션을 메시지 페이로드로 가지는 메시지를 분할하여, 각각 제품의 페이로드를 갖는 다수의 메시지로 분배
//	2. 하나의 제품 페이로드를 가지는 메시지의 여러 정보들을, 서로 다른 하위 플로우에서 처리하도록 분배
	
//	public class OrderSplitter {
//		public Collection<Object> splitOrderIntoParts(PurchaseOrder po) {
//			ArrayList<Object> parts = new ArrayList<>();
//			parts.add(po.getBillingInfo());		// 대금 청구 정보와 주문 항목 리스트, 두가지 메시지로 분할
//			parts.add(po.getLineItems());
//			return parts;
//		}
//	}

//	통합 플로우의 일부로, 분배기 빈을 선언
//	주문 메시지가 poChannel로 들어오고, OrderSplitter에 의해 분할, 
//	컬렉션으로 반환되는 각 항목은 output 채널에, 별도의 메시지로 전달된다.
//	@Bean
//	@Splitter(inputChannel="poChannel", outputChannel="splitOrderChannel") 
//	public OrderSplitter orderSplitter() {
//		return new OrderSplitter();
//	}
	
//	라우터를 이용해, 페이로드 타입을 기반으로, 각 정보에 적합한 하위 플로우로 분배
//	@Bean
//	@Router(inputChannel="splitOrderChannel")
//	public MessageRouter splitOrderRouter() {
//		PayloadTypeRouter router = new PayloadTypeRouter();
//		router.setChannelMapping(BillingInfo.class.getName(), "billingInfoChannel");
//		router.setChannelMapping(List.class.getName(), "lineItemsChannel");
//		return router;
//	}

//	이렇게하면, 컬렉션에 저장된 각 LineItem 들은 output 채널로 전달된다.
//	@Splitter(inputChannel="lineItemsChannel", outputChannel="lineItemChannel")
//	public List<LineItem> lineItemSplitter(List<LineItem> lineItems){
//		return lineItems;
//	}

//	자바 DSL을 사용해 위와 동일한 분배기 / 라우터 구성
//	return IntegrationFlows
//			...
//			.split(orderSplitter())
//			.<Object, String> route( 
//			p -> {
//				if (p.getClass().isAssignableFrom(BillingInfo.class)) { return "BILLING_INFO"; }
//				else { return "LINE_ITEMS"; }}, 
//			mapping -> mapping
//			.subFlowMapping("BILLING_INFO", sf -> sf.<BillingInfo> handle((billingInfo,h) -> { ... }))
//			.subFlowMapping("LINE_ITEMS", sf -> sf.split().<LineItem> handle((lineItem, h) -> { ... }))
//			)
//			.get();
//	
	
	
//	!! 서비스 액티베이터
	
//	서비스 액티베이터는 입력 채널로부터 메시지를 수신하고, 이 메시지를 MessageHandler 인터페이스를 구현한 클래스 (빈) 에 전달한다.
//	이 서비스 액티베이터는 input 채널에서 메시지를 받으면 , 메시지 페이로드를 표준 출력 스트림으로 내보낸다.
	
//	@Bean
//	@ServiceActivator(inputChannel="someChannel")
//	public MessageHandler sysoutHandler() {
//		return message -> { System.out.println("Message payload: " + message.getPayload()); };
//	}

//	이 서비스 액티베이터는 Order 타입의 메시지 페이로드를 처리하는 GenericHandler를 구현하며, 메시지가 도착하면 리포지토리를 통해 저장한다.
//	그리고 저장된 Order 객체가 반환되면 output 채널로 전달된다.
//	GenericHandler를 구현하기 때문에, 받은 메시지의 데이터를 처리한 후 새로운 페이로드를 반환할 수 있으며, 페이로드는 물론이고, 메시지 헤더도 받는다.
	
//	@Bean
//	@ServiceActivator(inputChannel="orderChannel", outputChannel="completeChannel")
//	public GenericHandler<Order> orderHandler(OrderRepository orderRepo) {
//		return (payload, headers) -> { return orderRepo.save(payload); };
//	}

//	메시지를 매개변수로 받는 람다를 사용한다. (메서드 참조 또는 MessageHandler 인터페이스를 구현하는 클래스 인스턴스도 인자로 넣을 수 있다)
//	public IntegrationFlow someFlow() {
//		return IntegrationFlows
//				...
//					.handle(msg -> { System.out.println("Message payload: " + msg.getPayload()); })
//					.get();
//	}
	
//	서비스 액티베이터를 플로우의 제일 끝에 두지 않는다면, handle() 메서드에서 GenericHandler를 인자로 받을 수도 있다.
//	public IntegrationFlow orderFlow(OrderRepository orderRepo) {
//		return IntegrationFlows
//				...
//					.<Order>handle((payload, headers) -> { return orderRepo.save(payload); })
//				...
//					.get();
//	}
	
	
//	!! 게이트웨이

//	게이트웨이는 애플리케이션이 통합 플로우로 메시지(데이터)를 전송하고, 선택적으로 폴로우의 처리결과인 응답을 받는데 사용
	
//	양방향 게이트웨이
//	이렇게 게이트웨이 인터페이스를 정의만하면, 이 인터페이스 구현체는 스프링 통합이 런타임 시에 자동으로 제공
//	@Component
//	@MessagingGateway(defaultRequestChannel="inChannel", defaultReplyChannel="outChannel")
//	public interface UpperCaseGateway {
//		String uppercase(String in);
//	}
	
//	@Bean
//	public IntegrationFlow uppercaseFlow() {
//		return IntegrationFlows
//				.from("inChannel")
//				.<String, String> transform(s -> s.toUpperCase())
//				.channel("outChannel")
//				.get();
//	}
	

//	!! 채널 어댑터
	
//	채널 어댑터는 통합 플로우의 입구와 출구를 나타낸다.
//	데이터는 인바운드 채널 어댑터를 통해 통합 플로우로 들어오고, 아웃바운드 채널 어댑터를 통해 통합 플로우에서 나간다.
//	이 빈은 주입된 AtomicInteger로부터 채널로 매초마다 한번씩 숫자를 전달한다.
//	@Bean
//	@InboundChannelAdapter(poller=@Poller(fixedRate="1000"), channel="numberChannel")
//	public MessageSource<Integer> numberSource(AtomicInteger source){
//		return () -> {
//			return new GenericMessage<>(source.getAndIncrement());
//		};
//	}
//	@Bean
//	public IntegrationFlow someFlow(AtomicInteger integerSource) {
//		return IntegrationFlows
//				.from(integerSource, "getAndIncrement", c -> c.poller(Pollers.fixedRate(1000)))
//				...
//				.get();
//	}

//	종종 채널 어댑터는 스프링 통합의 여러 엔드포인트 모듈 중 하나에서 제공된다.
//	예로, 지정된 디렉터리를 모니터링해, 저장되는 파일을 file-channel이라는 이름의 채널에 메시지로 전달하는 인바운드 채널 어댑터
//	@Bean
//	@InboundChannelAdapter(channel="file-channel", poller=@Poller(fixedDelay="1000"))
//	public MessageSource<File> fileReadingMessageSource() {
//		FileReadingMessageSource sourceReader = new FileReadingMessageSource();
//		sourceReader.setDirectory(new File(INPUT_DIR));
//		sourceReader.setFilter(new SimplePatternFileListFilter(FILE_PATTERN));
//		return sourceReader;
//	}
//	
//	@Bean
//	public IntegrationFlow fileReaderFlow() {
//		return IntegrationFlows
//				.from(Files.inboundAdapter(new File(INPUT_DIR))
//				.patternFilter(FILE_PATTERN))
//				.get();
//	}


//	!! 엔드포인트 모듈

//	스프링 통합이, 우리 나름의 채널 어댑터를 생성할 수 있게 해주지만,
//	다양한 외부 시스템과의 통합을 위해, 채널 어댑터가 포함된 24개 이상의 엔드포인트 모듈 (인바운드, 아운바운드 모두) 을 제공한다.

//	AMQP, event(스프링 애플리케이션 이벤트), feed(RSS와 Atom), file(파일 시스템), FTP, HTTP, JDBC, JPA, JMS, mail(이메일), Redis, Syslog 
//	WebFlux, WebSocket, ZooKeepe 등등
	
	
	
}