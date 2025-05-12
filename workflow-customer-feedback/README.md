该服务提供了一个客户评价分类处理的接口，根据对话信息，返回归类结果。

http://localhost:18080/customer/chat?query=我收到的产品有快递破损，需要退换货？
```json {"keywords": ["产品", "快递", "破损", "退换货"], "category_name": "after-sale service"} ```

http://localhost:18080/customer/chat?query=我的产品不能正常工作了，要怎么去做维修？
```json [ {"keywords": ["产品", "正常工作", "维修"], "category_name": "after-sale service"} ] ```

http://localhost:18080/customer/chat?query=商品收到了，非常好，下次还会买。
Praise, no action taken.

The workflow chat shows below.
- We first use a feedback_classifier to determine the is positive or negative;
- If it is positive, we just use recorder to write a log;
- If it is negative, we should provide the customer a solution to the specific problem;
- Before providing a solution, we use specific_question_classifier to determine what kind of the problem the negative feedback is;
- The specific_question_classifier classify the problem by 4 types: "after-sale service", "transportation", "product quality", "others"
- We provide the specific solution for the specific feedback(to be implemented, record a log for now)

```mermaid
flowchart TD
	__START__((start))
	__END__((stop))
	feedback_classifier("feedback_classifier")
	specific_question_classifier("specific_question_classifier")
	recorder("recorder")
	condition1{"check state"}
	condition2{"check state"}
	__START__:::__START__ --> feedback_classifier:::feedback_classifier
	feedback_classifier:::feedback_classifier -.-> condition1:::condition1
	condition1:::condition1 -.->|negative| specific_question_classifier:::specific_question_classifier
	%%	feedback_classifier:::feedback_classifier -.->|negative| specific_question_classifier:::specific_question_classifier
	condition1:::condition1 -.->|positive| recorder:::recorder
	%%	feedback_classifier:::feedback_classifier -.->|positive| recorder:::recorder
	specific_question_classifier:::specific_question_classifier -.-> condition2:::condition2
	condition2:::condition2 -.->|transportation| recorder:::recorder
	%%	specific_question_classifier:::specific_question_classifier -.->|transportation| recorder:::recorder
	condition2:::condition2 -.->|quality| recorder:::recorder
	%%	specific_question_classifier:::specific_question_classifier -.->|quality| recorder:::recorder
	condition2:::condition2 -.->|after-sale| recorder:::recorder
	%%	specific_question_classifier:::specific_question_classifier -.->|after-sale| recorder:::recorder
	condition2:::condition2 -.->|others| recorder:::recorder
	%%	specific_question_classifier:::specific_question_classifier -.->|others| recorder:::recorder
	recorder:::recorder --> __END__:::__END__

	classDef ___START__ fill:black,stroke-width:1px,font-size:xx-small;
	classDef ___END__ fill:black,stroke-width:1px,font-size:xx-small;
```