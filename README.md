# Thread
- 요구사항: 기다려야하는 작업(API 호출)을 동시에 어려번 호출하고 싶을때
- 해결: Thread를 사용해서 응답을 기다리지 않고 여러번 호출
```bash
 ./gradlew runThread
```
```java
for (int i=0; i<10; i++) {
    new Thread(() -> service.sendRequest(1, 2)).start();
}
```
- 문제: 작업할때마다 스레드 생성, 이 스레드를 관리하고 싶음!
- [소스코드](https://github.com/ChangguHan/java-async/blob/main/src/main/java/org/example/javaasync/n1_thread/ThreadApplication.java)


# Executor Service
- 요구사항: 
  - 기다려야하는 작업(API 호출)을 동시에 어려번 호출하고 싶을때 
  - 스레드를 관리하고 싶음
- 해결: ExecutorService 사용
  - ![img1](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*pJSPRuDqTxztOsOhhvkYxA.png)
  - ![img2](https://codepumpkin.com/wp-content/uploads/2017/06/ExecutorFramework.png.webp)
```bash
./gradlew runExecutorService
```
```java
final ExecutorService executorService = Executors.newFixedThreadPool(5);
for (int i=0; i<10; i++) {
    executorService.submit(() -> service.sendRequest(1, 2));
}
```
- 추가 요구사항: 비동기 작업의 결과값을 받아오고 싶음
- [소스코드](https://github.com/ChangguHan/java-async/blob/main/src/main/java/org/example/javaasync/n2_executorservice/ExecutorServiceApplication.java)

# Future
- 요구사항:
    - 기다려야하는 작업(API 호출)을 동시에 어려번 호출하고 싶을때
    - 스레드를 관리하고 싶음
    - 비동기 작업의 결과값을 받아오고 싶음
- 해결: Future 사용해서, get()으로 가져옴
```bash
./gradlew runFuture
```
```java
final ExecutorService executorService = Executors.newFixedThreadPool(5);
for (int i=0; i<10; i++) {
    final Future<Integer> future = executorService.submit(() -> service.sendRequest(1, 2));
    log.info(String.valueOf(future.get()));
}
```
- 문제 : get()을 하는순간 기다리기 때문에 비동기의 의마가 사라짐
- [소스코드](https://github.com/ChangguHan/java-async/blob/main/src/main/java/org/example/javaasync/n3_future/FutureApplication.java)

# CompletableFuture
- 요구사항:
    - 기다려야하는 작업(API 호출)을 동시에 어려번 호출하고 싶을때
    - 스레드를 관리하고 싶음
    - 비동기 작업의 결과값을 받아오고 싶음
- 해결: CompletableFuture 사용해서, 콜백에서 값을 사용가능
```bash
./gradlew runCompletableFuture1
```
```java
final ExecutorService executorService = Executors.newFixedThreadPool(5);
for (int i=0; i<10; i++) {
    CompletableFuture.supplyAsync(() -> service.sendRequest(random.nextInt(100), random.nextInt(100)), executorService)
        .thenAccept(t -> log.info(String.valueOf(t)));
}
```
- 새로운 요구사항 : 이전의 비동기 작업의 결과물로 다시한번 비동기 작업 호출
- [소스코드](https://github.com/ChangguHan/java-async/blob/main/src/main/java/org/example/javaasync/n4_completableFuture/CompletableFutureApplication.java)

# CompletableFuture-2
- 요구사항:
    - 기다려야하는 작업(API 호출)을 동시에 어려번 호출하고 싶을때
    - 스레드를 관리하고 싶음
    - 비동기 작업의 결과값을 받아와서 다시 비동기 작업 호출
- 해결: CompletableFuture의 thenCompose() 사용
```bash
./gradlew runCompletableFuture2
```
```java
final ExecutorService executorService = Executors.newFixedThreadPool(5);
final var completableFuture1 = CompletableFuture.supplyAsync(() -> service.sendRequest(random.nextInt(100), random.nextInt(100)), executorService);
completableFuture1.thenCompose(
    t -> CompletableFuture.supplyAsync(() -> service.sendRequest(t, random.nextInt(100)), executorService)
).thenAccept(t -> log.info(String.valueOf(t)));
```
- 새로운 요구사항 : 여러개의 비동기 작업의 결과물을 가지고 다시 비동기 호출
- [소스코드](https://github.com/ChangguHan/java-async/blob/main/src/main/java/org/example/javaasync/n5_completableFuture/CompletableFutureApplication2.java)

# CompletableFuture-3
- 요구사항:
    - 기다려야하는 작업(API 호출)을 동시에 어려번 호출하고 싶을때
    - 스레드를 관리하고 싶음
    - 여러개의 비동기 작업의 결과물을 가지고 다시 비동기 호출
- 해결: CompletableFuture의 thenCombine() 사용
```bash
./gradlew runCompletableFuture3
```
```java
final ExecutorService executorService = Executors.newFixedThreadPool(5);
final var completableFuture1 = CompletableFuture.supplyAsync(() -> service.sendRequest(random.nextInt(100), random.nextInt(100)), executorService);
final var completableFuture2 = CompletableFuture.supplyAsync(() -> service.sendRequest(random.nextInt(100), random.nextInt(100)), executorService);
completableFuture1.thenCombine(completableFuture2, (t1, t2) -> service.sendRequest(t1, t2))
    .thenAccept(t -> log.info(String.valueOf(t)));
```
- 문제: 코드에 스레드풀 관리와 비즈니스 코드가 섞여있음
- [소스코드](https://github.com/ChangguHan/java-async/blob/main/src/main/java/org/example/javaasync/n6_completableFuture/CompletableFutureApplication3.java)

# Spring Async
- 요구사항:
    - 기다려야하는 작업(API 호출)을 동시에 어려번 호출하고 싶을때
    - 스레드를 관리하고 싶음
    - 비동기 관리 코드와 비즈니스 코드를 분리
- 해결: Spring의 @Async, Executor Bean 사용
```bash
./gradlew runSpringAsync
```
```java
@Configuration
@EnableAsync
public static class AsyncConfiguration {

    @Bean(destroyMethod = "shutdown")
    public Executor asyncExecutor() {
        final var executor = new ThreadPoolTaskExecutorBuilder()
                .corePoolSize(10)
                .maxPoolSize(10)
                .threadNamePrefix("CustomTP-")
                .build();
        executor.initialize();

        return executor;
    }
}

@Service
@RequiredArgsConstructor
public static class PlusAsyncService {
    private final PlusService plusService;
    private final Executor asyncExecutor;

    @Async("asyncExecutor")
    public void sendRequest(int param1, int param2) {
        plusService.sendRequest(param1, param2);
    }

    public CompletableFuture<Integer> sendRequestCompletable(int param1, int param2) {
        return CompletableFuture.supplyAsync(() -> plusService.sendRequest(param1, param2), asyncExecutor);
    }
}
```
- [소스코드](https://github.com/ChangguHan/java-async/blob/main/src/main/java/org/example/javaasync/n7_springasync/SpringAsyncApplication.java)
