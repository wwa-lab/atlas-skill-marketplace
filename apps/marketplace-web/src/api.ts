export type Rating = { state: 'RATED' | 'UNRATED'; value: number | null; count: number }
export type SkillCard = { id:string; slug:string; displayName:string; description:string; ownerDisplayName:string; category:{code:string;label:string}; tags:string[]; latestPublishedVersion:string|null; certificationState:string; rating:Rating; access:string; curatedPreview:string|null }
export type SearchResponse = { items:SkillCard[]; page:number; size:number; totalItems:number; totalPages:number }
export type SkillDetail = { skill:SkillCard; curatedReport:string|null; repositoryRef:string; versions:Array<{id:string;semanticVersion:string;gitTag:string;changelog:string;certificationState:string}> }
export type ApiProblem = { error:{code:string;message:string;correlationId:string} }

async function request<T>(path:string, init?:RequestInit):Promise<T>{
  const response=await fetch(path,{credentials:'same-origin',headers:{'Content-Type':'application/json',...(init?.headers??{})},...init})
  if(!response.ok){const problem=await response.json().catch(()=>({error:{message:'The request failed.'}}));throw new Error(problem.error?.message??'The request failed.')}
  return response.json() as Promise<T>
}
export const api={
  search:(q:string,category:string)=>request<SearchResponse>(`/api/v1/skills?q=${encodeURIComponent(q)}&category=${encodeURIComponent(category)}`),
  detail:(slug:string)=>request<SkillDetail>(`/api/v1/skills/${encodeURIComponent(slug)}`),
  createOperation:(key:string,body:unknown)=>request<{operationId:string;state:string;message:string;statusUrl:string}>('/api/v1/installations/operations',{method:'POST',headers:{'Idempotency-Key':key},body:JSON.stringify(body)}),
  operation:(id:string)=>request<{operationId:string;state:string;message:string}>(`/api/v1/operations/${encodeURIComponent(id)}`)
}
