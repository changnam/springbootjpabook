# springbootjpabook

dependency 는 web, h2, data jpa 3가지 선택
application.properties 에 datasource , hibernate jpa 속성, h2 console 관련설정
entity 클래스와 repository 인터페이스 생성 - 실행하면 자동으로 테이블이 생성된다
resources/data.sql 을 만들어 놓으면 자동 실행된다.

DataSourceAutoConfiguration.class 가 적용되었을 시 , 적용되는 것이며, exclude={DataSourceAutoConfiguration.class} 적용시 자동 sql은 수행되지 않음. 수행하고 싶으면 datasourceinitializer 를 사용할것.

spring.jpa.xxx 옵션은 HibernateJpaAutoConfiguration.class 사용시 적용됨( EntityManagerFactory 를 생성하며, 이때 속성으로 사용된다). disable 시 수동설정에서 값들을 셋팅해줘야 한다.

hibernate.hbm2ddl.auto 가 create, create-drop 인경우, classpath 에 import.sql 이 있으면 수행함. (중요, 이때 각 sql 이 한줄씩 작성되어야 오류가 없다) 
