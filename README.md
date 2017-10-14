# RxBus
Event Bus By RxJava2。早于自己项目中用到Rxjava2，而作者的这个项目 (https://github.com/AndroidKnife/RxBus) 中用到的Rxjava1，且很长时间没更新到使用Rxjava2.故在原项目上改成使用Rxjava2.

## Usage

Just 2 Steps:

### STEP 1

Add dependency to your gradle file:

```
compile 'com.lgr.rxbus2:rxbus:1.0.0'
```

### Or maven:

```
<dependency>
  <groupId>com.lgr.rxbus2</groupId>
  <artifactId>rxbus</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

TIP: Maybe you also use the JakeWharton/timber to log your message, you may need to exclude the timber (from version 1.0.4, timber dependency update from AndroidKnife/Utils/timber to JakeWharton):
```
compile ('compile 'com.lgr.rxbus2:rxbus:1.0.0'') {
    exclude group: 'com.jakewharton.timber', module: 'timber'
}
```
en Snapshots of the development version are available in Sonatype's snapshots repository.

### STEP 2

Just use the provided(Any Thread Enforce):

com.hwangjr.rxbus.RxBus
Or make RxBus instance is a better choice:
```
public static final class RxBus {
    private static Bus sBus;
    
    public static synchronized Bus get() {
        if (sBus == null) {
            sBus = new Bus();
        }
        return sBus;
    }
}
```
Add the code where you want to produce/subscribe events, and register and unregister the class.
```
public class MainActivity extends AppCompatActivity {
    ...
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        RxBus.get().register(this);
        ...
    }
    
    @Override
    protected void onDestroy() {
        ...
        RxBus.get().unregister(this);
        ...
    }
        
    @Subscribe
    public void eat(String food) {
        // purpose
    }
        
    @Subscribe(
        thread = EventThread.IO,
        tags = {
            @Tag(BusAction.EAT_MORE)
        }
    )
    public void eatMore(List<String> foods) {
        // purpose
    }
    
    @Produce
    public String produceFood() {
        return "This is bread!";
    }
    
    @Produce(
        thread = EventThread.IO,
        tags = {
            @Tag(BusAction.EAT_MORE)
        }
    )
    public List<String> produceMoreFood() {
        return Arrays.asList("This is breads!");
    }
    
    public void post() {
        RxBus.get().post(this);
    }
    
    public void postByTag() {
        RxBus.get().post(Constants.EventType.TAG_STORY, this);
    }
    ...
}
```

