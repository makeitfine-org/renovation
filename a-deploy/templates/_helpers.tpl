{{/*metadata:*/}}
{{/*  name: {{ include "mychart.fullname" . }}*/}}

{{- define "mychart.fullname" -}}
{{- printf "%s-%s" .Release.Name .Chart.Name | trunc 63 | trimSuffix "-" -}}
{{- end -}}


{{/*metadata:*/}}
{{/*  labels:*/}}
{{/*    {{ include "mychart.labels" . | nindent 4 }}*/}}

{{- define "mychart.labels" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}


{{/*spec:*/}}
{{/*  selector:*/}}
{{/*    matchLabels:*/}}
{{/*      {{ include "mychart.selectorLabels" . | nindent 6 }}*/}}

{{- define "mychart.selectorLabels" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}


{{/*spec:*/}}
{{/*  containers:*/}}
{{/*    - name: {{ .Chart.Name }}*/}}
{{/*      image: {{ include "mychart.image" . }}*/}}

{{- define "mychart.image" -}}
{{ .Values.image.repository }}:{{ default .Chart.AppVersion .Values.image.tag }}
{{- end -}}


{{/*Helm will render the ConfigMap,*/}}
{{/*metadata:*/}}
{{/*  labels:*/}}
{{/*    {{ include "annotation.reread.configmap" . | nindent 2 }}*/}}

{{- define "annotation.reread.configmap" -}}
annotations:
  checksum/config: {{ include (print $.Template.BasePath "/configmap.yaml") . | sha256sum }}
{{- end -}}





