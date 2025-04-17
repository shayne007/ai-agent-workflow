this service is responsible for providing customer support to the users of the application.

this service is also a spring ai mcp client that connects to the spring ai mcp server to receive and send messages to the users.
- mcp client can query the tools provided by mcp server
- mcp client ask LLM chat bot to select the tool invocation
- we use @EnableAutoConfiguration(exclude = { SseHttpClientTransportAutoConfiguration.class }) to exclude the httpclient based configuration to avoid conflicts with the mcp client.

we need to start the mcp server first before starting the mcp client.
when mcp client started, we test the response:

we first start a dailog to get the session id:
POST http://localhost:8084/api/v1/dialog/start
{  
"userId": "user_123",  
"deviceType": "mobile",  
"channel": "web"  
}  
response:
{
"sessionId": "478b2280-944d-435f-94c2-764efc74dff8",
"responseText": "您好！我是AI智能助手，请问有什么可以帮您？",
"suggestedActions": [
"查询进度",
"申请贷款",
"咨询产品"
]
}
then we use the session id to continue the dialog:
POST http://localhost:8084/api/v1/dialog/continue

{  
"sessionId": "478b2280-944d-435f-94c2-764efc74dff8",  
"inputText": "查询用户id是user1234的征信报告",  
"language": "zh-CN"  
}  

response:
{
"responseText": "This is a response to: 查询用户id是user1234的征信报告用户ID为 `user1234` 的征信报告如下：\n\n- **信用评分**: 720\n- **贷款历史**:\n  - 贷款金额: 50,000.0，状态: 已还清\n  - 贷款金额: 10,000.0，状态: 进行中\n\n该数据来自缓存。",
"suggestedActions": [
"Option 1",
"Option 2"
],
"type": "TEXT"
}
we can see the response text and suggested actions.
the response text is the response from the mcp server.